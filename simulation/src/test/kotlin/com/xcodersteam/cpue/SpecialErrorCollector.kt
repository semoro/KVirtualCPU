package com.xcodersteam.cpue

import org.hamcrest.Matcher
import org.junit.Assert
import org.junit.rules.Verifier
import org.junit.runners.model.MultipleFailureException
import java.util.*

/**
 * Created by Semoro on 02.10.16.
 * ©XCodersTeam, 2016
 */
class SpecialErrorCollector() : Verifier() {
    private val errors = ArrayList<Throwable>()

    @Throws(Throwable::class)
    override fun verify() {
        MultipleFailureException.assertEmpty(this.errors)
    }

    fun addError(error: Throwable) {
        this.errors.add(error)
    }


    fun <T> checkThat(value: T, matcher: Matcher<T>, reason: String = ""): Boolean {
        return this.checkSucceeds({ Assert.assertThat(reason, value, matcher) }) != null
    }

    fun <T> checkSucceeds(callable: () -> T): T? {
        try {
            return callable.invoke()
        } catch (var3: Throwable) {
            this.addError(var3)
            return null
        }
    }

}