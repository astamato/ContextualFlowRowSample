package com.example.contextualflowrowsample.data

class ProgrammingLanguageRepository {
  fun getProgrammingLanguages(): List<ProgrammingLanguage> {
    return listOf(
      ProgrammingLanguage("kotlin", "Kotlin"),
      ProgrammingLanguage("java", "Java"),
      ProgrammingLanguage("cpp", "C++"),
      ProgrammingLanguage("python", "Python"),
      ProgrammingLanguage("go", "GO"),
      ProgrammingLanguage("javascript", "Javascript"),
      ProgrammingLanguage("dart", "Dart"),
      ProgrammingLanguage("csharp", "C#"),
      ProgrammingLanguage("swift", "Swift"),
      ProgrammingLanguage("rust", "Rust"),
      ProgrammingLanguage("typescript", "TypeScript"),
      ProgrammingLanguage("php", "PHP"),
      ProgrammingLanguage("ruby", "Ruby"),
      ProgrammingLanguage("scala", "Scala"),
      ProgrammingLanguage("r", "R"),
      ProgrammingLanguage("perl", "Perl"),
      ProgrammingLanguage("lua", "Lua"),
      ProgrammingLanguage("haskell", "Haskell"),
      ProgrammingLanguage("clojure", "Clojure"),
      ProgrammingLanguage("elixir", "Elixir"),
    )
  }
}
