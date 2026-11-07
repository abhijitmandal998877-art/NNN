package com.example

import com.example.data.MotivationProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun `verify milestones sequence and targets`() {
        val milestones = MotivationProvider.MILESTONES
        assertEquals(10, milestones.size)
        assertEquals(1, milestones[0].targetDays)
        assertEquals(3, milestones[1].targetDays)
        assertEquals(7, milestones[2].targetDays)
        assertEquals(14, milestones[3].targetDays)
        assertEquals(21, milestones[4].targetDays)
        assertEquals(30, milestones[5].targetDays)
        assertEquals(60, milestones[6].targetDays)
        assertEquals(90, milestones[7].targetDays)
        assertEquals(180, milestones[8].targetDays)
        assertEquals(365, milestones[9].targetDays)

        // Verify descriptions and biological benefits are non-empty
        milestones.forEach { m ->
            assertTrue(m.title.isNotBlank())
            assertTrue(m.badgeLabel.isNotBlank())
            assertTrue(m.description.isNotBlank())
            assertTrue(m.biologicalBenefit.isNotBlank())
        }
    }

    @Test
    fun `verify daily quotes and rotating index`() {
        val quotes = MotivationProvider.QUOTES
        assertTrue(quotes.size >= 10)

        val q1 = MotivationProvider.getDailyQuote(0)
        val q2 = MotivationProvider.getDailyQuote(1)
        assertNotNull(q1)
        assertNotNull(q2)
        assertTrue(q1.quote.isNotBlank())
        assertTrue(q1.author.isNotBlank())
    }

    @Test
    fun `verify discipline tips categories`() {
        val tips = MotivationProvider.TIPS
        assertTrue(tips.size >= 5)
        tips.forEach { tip ->
            assertTrue(tip.title.isNotBlank())
            assertTrue(tip.actionItem.isNotBlank())
            assertTrue(tip.category.isNotBlank())
        }
    }

    @Test
    fun `verify recovery timeline phases`() {
        val phases = MotivationProvider.RECOVERY_PHASES
        assertEquals(5, phases.size)
        phases.forEach { phase ->
            assertTrue(phase.days.isNotBlank())
            assertTrue(phase.phaseName.isNotBlank())
            assertTrue(phase.brainRecovery.isNotBlank())
            assertTrue(phase.mentalShift.isNotBlank())
        }
    }
}
