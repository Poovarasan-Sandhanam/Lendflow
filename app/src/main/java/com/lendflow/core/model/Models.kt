package com.lendflow.core.model

import java.time.LocalDate

// Plain UI-facing models. Phase 2 maps network DTOs and Room entities into these.

enum class LoanStatus { Active, PaidOff, InReview }

data class Loan(
    val id: String,
    val name: String,
    val purpose: String,
    val principal: Double,
    val balance: Double,
    val aprPercent: Double,
    val termMonths: Int,
    val paymentsMade: Int,
    val monthlyPayment: Double,
    val nextPaymentDate: LocalDate?,
    val status: LoanStatus,
) {
    /** 0f..1f share of the principal already repaid. */
    val progress: Float get() = (1 - balance / principal).toFloat().coerceIn(0f, 1f)
}

enum class TransactionKind { Repayment, Overpayment, Disbursement, Interest }

data class Transaction(
    val id: String,
    val loanId: String,
    val title: String,
    val date: LocalDate,
    val amount: Double,
    val kind: TransactionKind,
)

data class Notice(
    val id: String,
    val title: String,
    val body: String,
    val date: LocalDate,
    val unread: Boolean,
)

data class Customer(
    val firstName: String,
    val lastName: String,
    val email: String,
    val memberSince: Int,
    val creditScore: Int,
    val creditScoreMax: Int,
)
