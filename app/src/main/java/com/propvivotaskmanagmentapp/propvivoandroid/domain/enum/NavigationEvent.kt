package com.propvivotaskmanagmentapp.propvivoandroid.domain.enum

sealed class NavigationEvent {
    data class NavigateTo(val route: String) : NavigationEvent()
    object NavigateBack : NavigationEvent()
}
