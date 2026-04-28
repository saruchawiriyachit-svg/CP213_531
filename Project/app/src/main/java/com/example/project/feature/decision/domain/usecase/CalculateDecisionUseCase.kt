package com.example.project.feature.decision.domain.usecase

/**
 * Domain models for the decision engine.
 */
data class Option(
    val id: String,
    val name: String
)

data class Factor(
    val id: String,
    val name: String,
    val weight: Double // E.g., 1.0, 5.0, etc.
)

data class ScoreInput(
    val optionId: String,
    val factorId: String,
    val rawScore: Double // The score given before weight is applied
)

data class FactorScoreBreakdown(
    val factor: Factor,
    val rawScore: Double,
    val normalizedWeight: Double,
    val weightedScore: Double
)

data class OptionResult(
    val option: Option,
    val totalScore: Double,
    val breakdown: List<FactorScoreBreakdown>
)

data class DecisionEngineResult(
    val bestOption: Option?,
    val optionResults: List<OptionResult>
)

/**
 * A production-ready UseCase for calculating decision scores based on factored weighting.
 */
class CalculateDecisionUseCase {

    operator fun invoke(
        options: List<Option>,
        factors: List<Factor>,
        inputs: List<ScoreInput>
    ): DecisionEngineResult {
        // Edge case: No options provided
        if (options.isEmpty()) {
            return DecisionEngineResult(bestOption = null, optionResults = emptyList())
        }

        // Edge case: Single option provided
        if (options.size == 1) {
            val singleOption = options.first()
            val dummyBreakdown = factors.map { factor ->
                FactorScoreBreakdown(factor, 0.0, 0.0, 0.0)
            }
            val result = OptionResult(singleOption, 0.0, dummyBreakdown)
            return DecisionEngineResult(bestOption = singleOption, optionResults = listOf(result))
        }

        // Filter out negative weights and calculate total weight for normalization
        val validFactors = factors.map { it.copy(weight = it.weight.coerceAtLeast(0.0)) }
        val totalWeight = validFactors.sumOf { it.weight }

        // Edge case: All weights are zero. We can treat them as equally weighted.
        val isZeroWeight = totalWeight <= 0.0
        val normalizedFactors = validFactors.map { factor ->
            val normalizedWeight = if (isZeroWeight && validFactors.isNotEmpty()) {
                1.0 / validFactors.size
            } else if (validFactors.isNotEmpty()) {
                factor.weight / totalWeight
            } else {
                0.0
            }
            factor to normalizedWeight
        }.toMap()

        // Group inputs by Option ID and Factor ID for easy lookup
        val inputMap = inputs.associateBy { Pair(it.optionId, it.factorId) }

        // Calculate scores per option
        val optionResults = options.map { option ->
            var totalScore = 0.0
            val breakdownList = mutableListOf<FactorScoreBreakdown>()

            for ((factor, normalizedWeight) in normalizedFactors) {
                // Edge case: missing factor score defaults to 0.0
                val rawScore = inputMap[Pair(option.id, factor.id)]?.rawScore ?: 0.0
                val weightedScore = rawScore * normalizedWeight

                totalScore += weightedScore
                breakdownList.add(
                    FactorScoreBreakdown(
                        factor = factor,
                        rawScore = rawScore,
                        normalizedWeight = normalizedWeight,
                        weightedScore = weightedScore
                    )
                )
            }

            OptionResult(
                option = option,
                totalScore = totalScore,
                breakdown = breakdownList
            )
        }.sortedByDescending { it.totalScore }

        return DecisionEngineResult(
            bestOption = optionResults.firstOrNull()?.option,
            optionResults = optionResults
        )
    }
}
