package org.example.project.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.example.project.data.ProgrammingLanguage
import org.example.project.data.ProgrammingLanguageRepository

class ProgrammingLanguageViewModel(
  private val repository: ProgrammingLanguageRepository = ProgrammingLanguageRepository(),
) : ViewModel() {
  private val _programmingLanguages = MutableStateFlow<List<ProgrammingLanguage>>(emptyList())
  val programmingLanguages: StateFlow<List<ProgrammingLanguage>> = _programmingLanguages.asStateFlow()

  private val _selectedLanguages = MutableStateFlow<Set<String>>(emptySet())
  val selectedLanguages: StateFlow<Set<String>> = _selectedLanguages.asStateFlow()

  init {
    loadProgrammingLanguages()
  }

  private fun loadProgrammingLanguages() {
    viewModelScope.launch {
      _programmingLanguages.value = repository.getProgrammingLanguages()
    }
  }

  fun onLanguageSelected(languageId: String) {
    val currentSelected = _selectedLanguages.value.toMutableSet()
    if (currentSelected.contains(languageId)) {
      currentSelected.remove(languageId)
    } else {
      currentSelected.add(languageId)
    }
    _selectedLanguages.value = currentSelected
  }
}
