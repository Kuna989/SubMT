package pl.submt.data.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import pl.submt.data.local.SubMtDatabase
import pl.submt.domain.model.BillingCycle
import pl.submt.domain.model.SubscriptionCategory
import pl.submt.testing.subscription
import java.time.LocalDate

/** Repository + Room together: checks that the domain model survives the round trip. */
@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class OfflineSubscriptionRepositoryTest {

    private lateinit var database: SubMtDatabase
    private lateinit var repository: OfflineSubscriptionRepository

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, SubMtDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        repository = OfflineSubscriptionRepository(database.subscriptionDao(), UnconfinedTestDispatcher())
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun add_thenGet_returnsEqualDomainModel() = runTest {
        val original = subscription(
            name = "iCloud",
            amountMinor = 499,
            currency = "USD",
            cycle = BillingCycle.QUARTERLY,
            firstPaymentDate = LocalDate.of(2024, 2, 29),
            category = SubscriptionCategory.CLOUD,
            reminderDaysBefore = null,
            notes = "200 GB",
            isActive = false,
        )

        val id = repository.addSubscription(original)

        assertEquals(original.copy(id = id), repository.getSubscription(id))
    }

    @Test
    fun observeActive_filtersCancelled() = runTest {
        repository.addSubscription(subscription(name = "Active"))
        repository.addSubscription(subscription(name = "Cancelled", isActive = false))

        assertEquals(listOf("Active"), repository.observeActiveSubscriptions().first().map { it.name })
        assertEquals(2, repository.observeSubscriptions().first().size)
    }

    @Test
    fun update_andDelete_reportWhetherRowExisted() = runTest {
        val id = repository.addSubscription(subscription())

        assertTrue(repository.updateSubscription(subscription(id = id, amountMinor = 1)))
        assertFalse(repository.updateSubscription(subscription(id = 999)))
        assertTrue(repository.deleteSubscription(id))
        assertFalse(repository.deleteSubscription(id))
        assertNull(repository.observeSubscription(id).first())
    }

    @Test
    fun add_withExistingId_keepsIt() = runTest {
        val id = repository.addSubscription(subscription(id = 77))

        assertEquals(77L, id)
    }
}
