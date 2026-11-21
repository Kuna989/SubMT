package pl.submt.data.local.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import pl.submt.data.local.SubMtDatabase
import pl.submt.data.local.entity.SubscriptionEntity
import pl.submt.domain.model.BillingCycle
import pl.submt.domain.model.SubscriptionCategory
import java.time.LocalDate

/** Runs on the JVM with Robolectric against an in-memory SQLite database. */
@RunWith(AndroidJUnit4::class)
class SubscriptionDaoTest {

    private lateinit var database: SubMtDatabase
    private lateinit var dao: SubscriptionDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, SubMtDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = database.subscriptionDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insert_thenGetById_returnsSameData() = runTest {
        val id = dao.insert(subscription(name = "Netflix"))

        val loaded = dao.getById(id)

        assertEquals(subscription(name = "Netflix").copy(id = id), loaded)
    }

    @Test
    fun insert_generatesDistinctIds() = runTest {
        val first = dao.insert(subscription(name = "Netflix"))
        val second = dao.insert(subscription(name = "Spotify"))

        assertNotEquals(first, second)
    }

    @Test
    fun observeAll_isSortedByNameIgnoringCase() = runTest {
        dao.insert(subscription(name = "spotify"))
        dao.insert(subscription(name = "Netflix"))
        dao.insert(subscription(name = "apple Music"))

        val names = dao.observeAll().first().map { it.name }

        assertEquals(listOf("apple Music", "Netflix", "spotify"), names)
    }

    @Test
    fun observeActive_skipsInactiveSubscriptions() = runTest {
        dao.insert(subscription(name = "Active", isActive = true))
        dao.insert(subscription(name = "Cancelled", isActive = false))

        val names = dao.observeActive().first().map { it.name }

        assertEquals(listOf("Active"), names)
    }

    @Test
    fun update_changesStoredRow() = runTest {
        val id = dao.insert(subscription(name = "Netflix", amountMinor = 4_300))
        val updated = dao.getById(id)!!.copy(amountMinor = 6_000, billingCycle = BillingCycle.YEARLY)

        val rows = dao.update(updated)

        assertEquals(1, rows)
        assertEquals(updated, dao.getById(id))
    }

    @Test
    fun update_ofMissingRow_changesNothing() = runTest {
        val rows = dao.update(subscription(name = "Ghost").copy(id = 999))

        assertEquals(0, rows)
    }

    @Test
    fun deleteById_removesRow_andFlowEmitsUpdatedList() = runTest {
        val netflix = dao.insert(subscription(name = "Netflix"))
        dao.insert(subscription(name = "Spotify"))

        val rows = dao.deleteById(netflix)

        assertEquals(1, rows)
        assertNull(dao.getById(netflix))
        assertEquals(listOf("Spotify"), dao.observeAll().first().map { it.name })
        assertEquals(1, dao.observeCount().first())
    }

    @Test
    fun observeById_emitsNullForMissingSubscription() = runTest {
        assertNull(dao.observeById(42).first())
    }

    @Test
    fun optionalFields_canBeNull() = runTest {
        val id = dao.insert(subscription(name = "No reminder", reminderDaysBefore = null, notes = null))

        val loaded = dao.getById(id)!!

        assertNull(loaded.reminderDaysBefore)
        assertNull(loaded.notes)
    }

    private fun subscription(
        name: String,
        amountMinor: Long = 2_999,
        isActive: Boolean = true,
        reminderDaysBefore: Int? = 3,
        notes: String? = "Family plan",
    ) = SubscriptionEntity(
        name = name,
        amountMinor = amountMinor,
        currency = "PLN",
        billingCycle = BillingCycle.MONTHLY,
        firstPaymentDate = LocalDate.of(2026, 1, 15),
        category = SubscriptionCategory.STREAMING,
        reminderDaysBefore = reminderDaysBefore,
        notes = notes,
        isActive = isActive,
        createdAt = 1_768_435_200_000,
    )
}
