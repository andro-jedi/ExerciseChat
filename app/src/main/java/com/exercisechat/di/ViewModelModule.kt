package com.exercisechat.di

import com.exercisechat.ui.users.UsersViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::UsersViewModel)
}
