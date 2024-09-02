package com.abdulkadirkara.appshortcuts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {
    private val _shortcutType = MutableLiveData<ShortcutType?>()
    val shortcutType: LiveData<ShortcutType?> = _shortcutType

    fun setShortcutType(type: ShortcutType) {
        _shortcutType.value = type
    }
}
enum class ShortcutType {
    STATIC, DYNAMIC, PINNED
}