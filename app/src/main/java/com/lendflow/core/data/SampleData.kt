package com.lendflow.core.data

import com.lendflow.core.model.Customer
import com.lendflow.core.model.Loan
import com.lendflow.core.model.LoanStatus
import com.lendflow.core.model.Notice
import com.lendflow.core.model.Transaction
import com.lendflow.core.model.TransactionKind
import java.time.LocalDate

/**
 * Synthetic, hard-coded data so every screen has something realistic to render.
 * Phase 2 replaces this object with a Room-backed repository; screens keep the same models.
 */
object SampleData {

    /** Fixed "today" so day counts stay stable in previews and tests. */
    val today: LocalDate = LocalDate.of(2026, 10, 25)

    val customer = Customer(
        firstName = "Alex",
        lastName = "Morgan",
        email = "alex.morgan@example.com",
        memberSince = 2023,
        creditScore = 742,
        creditScoreMax = 999,
    )

    val loans = listOf(
        Loan(
            id = "car-2024",
            name = "Car loan",
            purpose = "Car",
            principal = 12_000.0,
            balance = 7_436.18,
            aprPercent = 7.4,
            termMonths = 48,
            paymentsMade = 19,
            monthlyPayment = 289.65,
            nextPaymentDate = today.plusDays(6),
            status = LoanStatus.Active,
        ),
        Loan(
            id = "home-2025",
            name = "Home improvement",
            purpose = "Home improvements",
            principal = 5_000.0,
            balance = 4_102.77,
            aprPercent = 9.9,
            termMonths = 36,
            paymentsMade = 7,
            monthlyPayment = 160.91,
            nextPaymentDate = today.plusDays(13),
            status = LoanStatus.Active,
        ),
        Loan(
            id = "debt-2022",
            name = "Debt consolidation",
            purpose = "Debt consolidation",
            principal = 3_500.0,
            balance = 0.0,
            aprPercent = 12.9,
            termMonths = 24,
            paymentsMade = 24,
            monthlyPayment = 165.71,
            nextPaymentDate = null,
            status = LoanStatus.PaidOff,
        ),
    )

    val transactions: List<Transaction> = buildList {
        var id = 0
        for (month in 0L..5L) {
            val date = today.minusMonths(month).withDayOfMonth(1)
            add(Transaction("t${id++}", "car-2024", "Car loan repayment", date, -289.65, TransactionKind.Repayment))
            add(Transaction("t${id++}", "home-2025", "Home improvement repayment", date.plusDays(7), -160.91, TransactionKind.Repayment))
        }
        add(Transaction("t${id++}", "car-2024", "Overpayment", today.minusMonths(2).withDayOfMonth(18), -500.0, TransactionKind.Overpayment))
        add(Transaction("t${id++}", "home-2025", "Loan paid out", today.minusMonths(7).withDayOfMonth(3), 5_000.0, TransactionKind.Disbursement))
    }.sortedByDescending { it.date }

    val notices = listOf(
        Notice("n1", "Repayment coming up", "£289.65 for your Car loan will be collected on 31 Oct.", today, unread = true),
        Notice("n2", "Your credit score went up", "Nice work — your score rose 12 points this month.", today.minusDays(3), unread = true),
        Notice("n3", "Overpayment received", "We've received your £500.00 overpayment. You'll finish 3 months sooner.", today.minusMonths(2), unread = false),
        Notice("n4", "Statement ready", "Your annual statement for your Debt consolidation loan is ready.", today.minusMonths(3), unread = false),
    )

    fun loan(id: String): Loan? = loans.firstOrNull { it.id == id }

    fun transactionsFor(loanId: String): List<Transaction> = transactions.filter { it.loanId == loanId }
}
