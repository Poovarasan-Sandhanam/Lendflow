package com.lendflow

import android.app.Application

/**
 * Process-wide entry point, created before any Activity.
 *
 * Empty for now. Phase 3 annotates it with @HiltAndroidApp so Hilt can build the
 * application-scoped dependency graph here.
 */
class LendFlowApplication : Application()
