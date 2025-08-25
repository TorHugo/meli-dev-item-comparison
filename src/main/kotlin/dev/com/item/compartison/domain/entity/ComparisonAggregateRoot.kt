package dev.com.item.compartison.domain.entity

import dev.com.item.compartison.domain.utils.DomainEntity
import java.util.UUID

data class ComparisonAggregateRoot(
    override val identifier: UUID = UUID.randomUUID(),
    val strategy: String,
    val winner: ComparisonWinnerAg,
    val ranking: List<ComparisonRankingAg>,
    val details: ComparisonDetailAg?,
    val rule: ComparisonRuleDomain?,
    val metadata: Map<String, Any>? = mapOf()
) : DomainEntity<UUID>()
