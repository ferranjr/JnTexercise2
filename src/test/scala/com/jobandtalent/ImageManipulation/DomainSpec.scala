package com.jobandtalent.ImageManipulation

import java.awt.color.{ColorSpace, ICC_ColorSpace}
import java.awt.image.{BufferedImage, ColorModel, DirectColorModel, WritableRaster}

import org.mockito.Mockito._
import org.mockito.stubbing.Answer
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{FlatSpec, Matchers}

import scala.collection.mutable

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
