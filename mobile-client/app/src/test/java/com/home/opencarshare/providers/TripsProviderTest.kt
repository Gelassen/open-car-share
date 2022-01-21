package com.home.opencarshare.providers

import junit.framework.TestCase

class TripsProviderTest : TestCase() {

    var subject = TripsProvider()

    fun testValidateDate() {
        var validDate = "1.01.2020 7:00"

        var result = subject.validateDate(validDate)

        assertEquals(true, result)
    }

    fun testUnvalidDateTime_validateDate_getFalse() {
        val dateTime = "21.01.2022"

        val parsed = subject.validateDate(dateTime)

        assertEquals(false, parsed)
    }

    fun testOnValidDateTime_dateTimeAsLongAndDateInUserFormat_getValidaDateTime() {
        var validDate = "01.01.2020 07:00"

        var parsed = subject.dateTimeAsLong(validDate)
        var restored = subject.dateInUserFormat(parsed)
        var parsedAgain = subject.dateTimeAsLong(restored)

        assertEquals(validDate, restored)
    }

    fun testOnValidDateTimeInAcceptableButNoCorrectFormat_dateTimeAsLongAndDateInUserFormat_getValidaDateTime() {
        var validDate = "1.01.2020 7:00"

        var parsed = subject.dateTimeAsLong(validDate)
        var restored = subject.dateInUserFormat(parsed)
        var parsedAgain = subject.dateTimeAsLong(restored)

        assertEquals(parsedAgain, parsed)
    }

    fun testUnvalidDateTime_dateTimeAsLongAndDateInUserFormat_getNotValidaDateTime() {
        var notValidDate = "1.01.2020"
        var parsed = subject.dateTimeAsLong(notValidDate)
        assertEquals(-1L, parsed)

        notValidDate = "7:00 1.01.2020"
        parsed = subject.dateTimeAsLong(notValidDate)
        assertEquals(-1L, parsed)

        notValidDate = "7 января 8:00"
        parsed = subject.dateTimeAsLong(notValidDate)
        assertEquals(-1L, parsed)

        notValidDate = "something absolutely wrong"
        parsed = subject.dateTimeAsLong(notValidDate)
        assertEquals(-1L, parsed)
    }

}