package com.example.data

import com.example.data.model.DailyQuote
import com.example.data.model.DisciplineTip
import com.example.data.model.Milestone
import com.example.data.model.RecoveryMilestonePhase

object MotivationProvider {

    val MILESTONES = listOf(
        Milestone(
            targetDays = 1,
            title = "The Decision",
            badgeLabel = "Day 1",
            description = "The courage to begin and interrupt the unconscious loop.",
            biologicalBenefit = "Dopamine receptors begin their initial cooldown. Cravings drop in frequency after bedtime."
        ),
        Milestone(
            targetDays = 3,
            title = "Breaking The Loop",
            badgeLabel = "Day 3",
            description = "Conquering the most acute 72-hour trigger window.",
            biologicalBenefit = "Willpower prefrontal circuits activate. Your mind recognizes you are in charge."
        ),
        Milestone(
            targetDays = 7,
            title = "One Week Free",
            badgeLabel = "Day 7",
            description = "A milestone achieved by only the dedicated few.",
            biologicalBenefit = "Androgen receptor density rebounds. Morning energy and mental drive noticeably surge."
        ),
        Milestone(
            targetDays = 14,
            title = "Mental Clarity",
            badgeLabel = "Day 14",
            description = "Brain fog begins lifting, replaced by focused attention.",
            biologicalBenefit = "Prolactin levels normalize; social anxiety recedes, and eye contact feels effortless."
        ),
        Milestone(
            targetDays = 21,
            title = "Habit Rewiring",
            badgeLabel = "Day 21",
            description = "The neurological threshold where new habits solidify.",
            biologicalBenefit = "Neural synaptic pathways of compulsive routines weaken; healthy routines take root."
        ),
        Milestone(
            targetDays = 30,
            title = "One Month Triumph",
            badgeLabel = "Day 30",
            description = "One full month of uncompromised self-respect.",
            biologicalBenefit = "Substantial dopamine baseline restoration. Natural appreciation for simple pleasures returns."
        ),
        Milestone(
            targetDays = 60,
            title = "Dopamine Reset",
            badgeLabel = "Day 60",
            description = "Profound self-confidence and unbreakable resilience.",
            biologicalBenefit = "Emotional stability solidifies; impulse resistance feels natural rather than strenuous."
        ),
        Milestone(
            targetDays = 90,
            title = "Reboot Master",
            badgeLabel = "Day 90",
            description = "The classical 90-day neuroplasticity transformation completed.",
            biologicalBenefit = "Full neuroplastic reboot achieved. Compulsive triggers lose their visceral power over your mind."
        ),
        Milestone(
            targetDays = 180,
            title = "Unbreakable Mind",
            badgeLabel = "Day 180",
            description = "Half a year of continuous mastery over cheap dopamine.",
            biologicalBenefit = "Executive function and long-term delayed gratification are your permanent default mindset."
        ),
        Milestone(
            targetDays = 365,
            title = "True Ascendance",
            badgeLabel = "1 Year",
            description = "One complete year of disciplined freedom and vitality.",
            biologicalBenefit = "Life transformed: complete reclamation of sexual energy into life creation, career, and physical vitality."
        )
    )

    val QUOTES = listOf(
        DailyQuote(
            quote = "No man is free who is not master of himself.",
            author = "Epictetus",
            context = "Stoic Wisdom"
        ),
        DailyQuote(
            quote = "Discipline is choosing between what you want now and what you want most.",
            author = "Abraham Lincoln",
            context = "Self-Command"
        ),
        DailyQuote(
            quote = "He who conquers others is strong; he who conquers himself is mighty.",
            author = "Lao Tzu",
            context = "Inner Strength"
        ),
        DailyQuote(
            quote = "You will never find peace until you become the master of your own desires.",
            author = "Marcus Aurelius",
            context = "Meditations"
        ),
        DailyQuote(
            quote = "The urge is a wave. You do not need to fight the wave; you just need to surf it until it dissolves.",
            author = "Alan Marlatt",
            context = "Urge Surfing"
        ),
        DailyQuote(
            quote = "The price of discipline is always less than the pain of regret.",
            author = "Nido Qubein",
            context = "Long-Term Vision"
        ),
        DailyQuote(
            quote = "First we make our habits, then our habits make us.",
            author = "Charles C. Noble",
            context = "Habit Formation"
        ),
        DailyQuote(
            quote = "Self-control is strength. Right thought is mastery. Calmness is power.",
            author = "James Allen",
            context = "As a Man Thinketh"
        ),
        DailyQuote(
            quote = "A moment of patience in a moment of temptation prevents a thousand moments of regret.",
            author = "Ancient Proverb",
            context = "Discipline"
        ),
        DailyQuote(
            quote = "Your mind will surrender 100 times before your body does. Train your mind to stay the course.",
            author = "David Goggins",
            context = "Mental Toughness"
        ),
        DailyQuote(
            quote = "When you learn to resist the easy dopamine, the world opens up its greatest rewards to you.",
            author = "Modern Science",
            context = "Neuroplasticity"
        ),
        DailyQuote(
            quote = "Between stimulus and response there is a space. In that space is our power to choose our response.",
            author = "Viktor E. Frankl",
            context = "Man's Search For Meaning"
        )
    )

    val TIPS = listOf(
        DisciplineTip(
            id = "tip_1",
            title = "The 10-Minute Urge Rule",
            summary = "Cravings naturally peak and subside within 10 to 15 minutes if you do not feed them with thoughts.",
            category = "Urge Shield",
            actionItem = "When an urge strikes, set a 10-minute timer. Walk into another room and drink a cold glass of water. Do not negotiate."
        ),
        DisciplineTip(
            id = "tip_2",
            title = "Device-Free Sleep Sanctuary",
            summary = "Over 80% of relapses happen late at night in bed with a smartphone within hand's reach.",
            category = "Environmental Design",
            actionItem = "Charge your phone outside the bedroom or at least 6 feet away from your bed before lying down."
        ),
        DisciplineTip(
            id = "tip_3",
            title = "Cold Water Shock Protocol",
            summary = "Cold water on your face triggers the mammalian dive reflex, immediately dropping your heart rate and calming compulsive urges.",
            category = "Physical Grounding",
            actionItem = "Splash freezing water on your face 5 times or take a brisk 60-second cold shower to reset your nervous system."
        ),
        DisciplineTip(
            id = "tip_4",
            title = "Transmute The Energy",
            summary = "Sexual energy is raw creative fuel. When bottled up, it seeks release. Channel it immediately into physical action.",
            category = "Habit Building",
            actionItem = "Drop and do 20 pushups, go for a fast brisk walk, or channel focus into a high-intensity task immediately."
        ),
        DisciplineTip(
            id = "tip_5",
            title = "Recognize HALT Triggers",
            summary = "Urges are rarely about physical need; they are emotional responses to Hungry, Angry, Lonely, or Tired states.",
            category = "Mental Clarity",
            actionItem = "Ask yourself: 'Am I actually aroused, or am I just bored, stressed, lonely, or tired?' Address the true emotional need."
        )
    )

    val RECOVERY_PHASES = listOf(
        RecoveryMilestonePhase(
            days = "Days 1 – 3",
            phaseName = "Withdrawal & Peak Cravings",
            brainRecovery = "Dopamine receptors cry out for cheap stimulation. Prefrontal willpower is tested.",
            mentalShift = "Accept discomfort as physical proof your brain is rewiring."
        ),
        RecoveryMilestonePhase(
            days = "Days 4 – 7",
            phaseName = "Physical Surge & Energy Return",
            brainRecovery = "Free androgen receptors increase. Testosterone baseline rebalances.",
            mentalShift = "Energy levels rise. You start noticing sharper mental presence in conversations."
        ),
        RecoveryMilestonePhase(
            days = "Days 8 – 21",
            phaseName = "Brain Fog Clearing & Deep Sleep",
            brainRecovery = "Prolactin drops back to normal. Sleep architecture deepens.",
            mentalShift = "Cravings become occasional rather than constant. Self-esteem rises significantly."
        ),
        RecoveryMilestonePhase(
            days = "Days 22 – 45",
            phaseName = "The Flatline / Emotional Reset",
            brainRecovery = "Dopamine baseline stabilizes. Cravings may lull, accompanied by calm stillness.",
            mentalShift = "Do not panic or test yourself. This quiet period is deep neural restoration."
        ),
        RecoveryMilestonePhase(
            days = "Days 46 – 90",
            phaseName = "Complete Neuroplastic Reboot",
            brainRecovery = "Receptor sensitivity restored to healthy human baseline.",
            mentalShift = "Freedom achieved. Compulsive masturbation is no longer who you are."
        )
    )

    fun getDailyQuote(epochDay: Long): DailyQuote {
        val index = (Math.abs(epochDay) % QUOTES.size).toInt()
        return QUOTES[index]
    }
}
