package com.example.project.feature.decision.domain.usecase

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class CalculateDecisionUseCaseTest {

    private lateinit var useCase: CalculateDecisionUseCase

    @Before
    fun setup() {
        useCase = CalculateDecisionUseCase()
    }

    @Test
    fun `invoke with valid data returns correct best option and normalized scores`() {
        val options = listOf(
            Option("o1", "Option A"),
            Option("o2", "Option B")
        )
        // Total weight is 10.0 (3.0 + 7.0)
        val factors = listOf(
            Factor("f1", "Cost", 3.0), // normalized weight: 0.3
            Factor("f2", "Quality", 7.0) // normalized weight: 0.7
        )
        val inputs = listOf(
            ScoreInput("o1", "f1", 8.0), // 8.0 * 0.3 = 2.4
            ScoreInput("o1", "f2", 6.0), // 6.0 * 0.7 = 4.2 -> total = 6.6
            
            ScoreInput("o2", "f1", 4.0), // 4.0 * 0.3 = 1.2
            ScoreInput("o2", "f2", 9.0)  // 9.0 * 0.7 = 6.3 -> total = 7.5
        )

        val result = useCase(options, factors, inputs)

        // Option B should have higher score (7.5 > 6.6)
        assertEquals("o2", result.bestOption?.id)
        
        val optionBResult = result.optionResults.first { it.option.id == "o2" }
        assertEquals(7.5, optionBResult.totalScore, 0.001)
        
        val optionAResult = result.optionResults.first { it.option.id == "o1" }
        assertEquals(6.6, optionAResult.totalScore, 0.001)
    }

    @Test
    fun `invoke with single option returns that option as best`() {
        val options = listOf(Option("o1", "Only Option"))
        val result = useCase(options, emptyList(), emptyList())
        
        assertEquals("o1", result.bestOption?.id)
        assertEquals(1, result.optionResults.size)
        assertEquals(0.0, result.optionResults.first().totalScore, 0.001)
    }

    @Test
    fun `invoke with empty options returns null best option`() {
        val result = useCase(emptyList(), emptyList(), emptyList())
        assertNull(result.bestOption)
        assertTrue(result.optionResults.isEmpty())
    }

    @Test
    fun `invoke handles zero weight factors by treating them equally`() {
        val options = listOf(
            Option("o1", "Option A"),
            Option("o2", "Option B")
        )
        // Both zero -> Total weight 0.0. Handled by equal distribution: 0.5 each
        val factors = listOf(
            Factor("f1", "Cost", 0.0), 
            Factor("f2", "Quality", 0.0)
        )
        val inputs = listOf(
            ScoreInput("o1", "f1", 10.0), // 10 * 0.5 = 5.0
            ScoreInput("o1", "f2", 0.0),  // 0 * 0.5 = 0.0 -> Total = 5.0

            ScoreInput("o2", "f1", 4.0),  // 4 * 0.5 = 2.0
            ScoreInput("o2", "f2", 8.0)   // 8 * 0.5 = 4.0 -> Total = 6.0
        )

        val result = useCase(options, factors, inputs)

        assertEquals("o2", result.bestOption?.id)
        assertEquals(2, result.optionResults.first().breakdown.size)
        assertEquals(0.5, result.optionResults.first().breakdown.first().normalizedWeight, 0.001)
    }

    @Test
    fun `invoke handles missing scores by defaulting to zero`() {
        val options = listOf(Option("o1", "Option A"), Option("o2", "Option B"))
        val factors = listOf(Factor("f1", "Criteria", 1.0))
        
        // o2 is missing input score for f1
        val inputs = listOf(
            ScoreInput("o1", "f1", 5.0)
        )

        val result = useCase(options, factors, inputs)
        
        // o1 has 5.0, o2 defaults to 0.0
        assertEquals("o1", result.bestOption?.id)
        assertEquals(5.0, result.optionResults.first { it.option.id == "o1" }.totalScore, 0.001)
        assertEquals(0.0, result.optionResults.first { it.option.id == "o2" }.totalScore, 0.001)
    }
}
