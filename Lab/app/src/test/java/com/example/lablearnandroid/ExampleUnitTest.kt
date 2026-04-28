package com.example.lablearnandroid

import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    // ฟังก์ชันนี้จะถูกเรียกทำงาน 1 ครั้ง "ก่อน" รันแต่ละ @Test
    @Before
    fun setUp() {
        // เตรียมข้อมูลหรือตั้งค่าที่ต้องใช้ก่อนทดสอบ (Arrange)
        println("Setting up the test...")
    }

    // ฟังก์ชันนี้จะถูกเรียกทำงาน 1 ครั้ง "หลัง" รันแต่ละ @Test
    @After
    fun tearDown() {
        // ล้างข้อมูลหรือคืนค่าเดิมหลังทดสอบเสร็จ
        println("Tearing down the test...")
    }

    // 1. ตัวอย่างการทดสอบค่าพื้นฐานทางคณิตศาสตร์
    @Test
    fun addition_isCorrect() {
        // Assertions มีหลายแบบ เช่น assertEquals ใช้เปรียบเทียบค่า
        assertEquals(4, 2 + 2)
        assertNotEquals(5, 2 + 2) // ต้องไม่เท่ากับ 5
    }

    // 2. ตัวอย่างการทดสอบการทำงานเกี่ยวกับ String
    @Test
    fun stringFormat_isCorrect() {
        val expected = "Hello, Android Developer"
        val actual = "Hello, " + "Android Developer"
        
        assertEquals(expected, actual)
        assertTrue(actual.contains("Android")) // คาดว่าต้องเป็น true
    }

    // 3. ตัวอย่างการทดสอบเกี่ยวกับลิสต์ (List)
    @Test
    fun listOperations_isCorrect() {
        val list = listOf("Pikachu", "Charmander", "Squirtle")
        
        assertNotNull(list)      // คาดว่าต้องไม่เป็น null
        assertEquals(3, list.size) // ตรวจสอบขนาดของลิสต์
        assertEquals("Pikachu", list[0]) // ตัวแรกต้องเป็น Pikachu
    }

    // 4. ตัวอย่างการตรวจสอบ Exception (เมื่อคาดหวังว่าต้องเกิด Error)
    @Test(expected = ArithmeticException::class)
    fun divideByZero_throwsException() {
        val result = 10 / 0 // ต้องทำให้เกิด ArithmeticException
    }
}