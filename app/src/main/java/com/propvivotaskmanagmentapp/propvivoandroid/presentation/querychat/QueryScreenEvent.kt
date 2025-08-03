package com.propvivotaskmanagmentapp.propvivoandroid.presentation.querychat

sealed class QueryScreenEvent {
    data class MessageChanged(val value: String) : QueryScreenEvent()
    object SendMessage : QueryScreenEvent()
}
