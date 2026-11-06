package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.repository.StreakRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `verify app name resource`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Stop Masturbate", appName)
    }

    @Test
    fun `verify streak repository relapse preserves best record and resets start`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val repo = StreakRepository(context)

        val threeDaysAgo = System.currentTimeMillis() - (3 * 24 * 60 * 60 * 1000L)
        repo.updateStartTime(threeDaysAgo)

        val settingsBefore = repo.settingsFlow.value
        assertTrue("Best streak should reflect past start time", settingsBefore.bestStreakMillis >= 2 * 24 * 60 * 60 * 1000L)

        // Record relapse
        val survived = repo.recordRelapse("Late Night in Bed", "I will keep phone out of room")
        assertTrue("Survived duration should be recorded", survived > 0L)

        val settingsAfter = repo.settingsFlow.value
        assertTrue("Best streak must be preserved or improved", settingsAfter.bestStreakMillis >= settingsBefore.bestStreakMillis)

        val relapses = repo.relapsesFlow.value
        assertEquals(1, relapses.size)
        assertEquals("Late Night in Bed", relapses[0].triggerReason)
    }

    @Test
    fun `verify json export and import roundtrip`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val repo = StreakRepository(context)

        repo.recordRelapse("Boredom", "Test note")
        val exportedJson = repo.exportToJson()
        assertNotNull(exportedJson)
        assertTrue(exportedJson.contains("Stop Masturbate"))
        assertTrue(exportedJson.contains("Boredom"))

        // Reset and then import
        repo.resetAllData()
        assertEquals(0, repo.relapsesFlow.value.size)

        val importResult = repo.importFromJson(exportedJson)
        assertTrue("Import must succeed", importResult.isSuccess)
        assertEquals(1, repo.relapsesFlow.value.size)
        assertEquals("Boredom", repo.relapsesFlow.value[0].triggerReason)
    }
}
