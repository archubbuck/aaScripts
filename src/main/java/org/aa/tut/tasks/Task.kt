package org.aa.tut.tasks

abstract class Task(
    val activationMessage: String,
    val successMessage: String,
    val failureMessage: String
) {
    abstract fun activate(): Boolean;
    abstract fun execute(): Boolean;
}