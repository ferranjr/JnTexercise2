package com.jobandtalent.ImageManipulation

import java.awt.image.BufferedImage

import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{FlatSpec, Matchers}

class DomainSpec
  extends FlatSpec
  with Matchers
  with MockitoSugar {

  val height = 1024
  val width = 1280

  "FreeImage" should "get the width from the BufferedImage" in {

    val bfi: BufferedImage = mock[BufferedImage]
    when(bfi.getWidth).thenReturn(width)

    val testFreeImage = FreeImage("path/image.jpg", bfi)
    testFreeImage.getWidth shouldBe width
  }

  it should "get the height from the BufferedImage" in {

    val bfi: BufferedImage = mock[BufferedImage]
    when(bfi.getHeight).thenReturn(height)

    val testFreeImage = FreeImage("path/image.jpg", bfi)
    testFreeImage.getHeight shouldBe height
  }

}
