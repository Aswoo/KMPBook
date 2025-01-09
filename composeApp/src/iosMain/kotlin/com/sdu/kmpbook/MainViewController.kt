package com.sdu.kmpbook

import androidx.compose.ui.window.ComposeUIViewController
import com.sdu.kmpbook.app.App
import com.sdu.kmpbook.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) { App() }