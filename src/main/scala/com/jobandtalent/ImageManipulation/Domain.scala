package com.jobandtalent.ImageManipulation

import java.awt.image.BufferedImage

/**
  * FreeImage
  * ~~~~
  * Case class to use as building block of our library,
  * is a really basic representation of an image, containing the
  * BufferedImage to simplify my demo.
  *
  * @param fullPath full path to the image
  * @param bufferedImage bufferedImage ready to be manipulated
  */
case class FreeImage(
  fullPath: String,
  bufferedImage: BufferedImage
) {

  /**
    * Retrieve the width of the image
    * @return
    */
  def getWidth: Int = bufferedImage.getWidth

  /**
    * Retrieve the height of the image
    * @return
    */
  def getHeight: Int = bufferedImage.getHeight

  /**
    * Retrieve the code for the color space
    * @return
    */
  def getColorSpaceType: Int = bufferedImage.getColorModel.getColorSpace.getType
}

/**
  * Small ADT to represent
  */
sealed trait Direction
case object Horizontal extends Direction
case object Vertical extends Direction


/**
  * Small ADT to represent the main exceptions of our library
  */
sealed abstract class FreeImageException(msg: String) extends Exception(msg)

case class FailedToLoadImage(imgSrc: String)
  extends FreeImageException(s"Unable to load image on path: '$imgSrc'")

case class FailedToSaveImage(imgDst: String)
  extends FreeImageException(s"Unable to store image on path: '$imgDst'")

case class FailedOperatingOverImage(operation: String)
  extends FreeImageException(s"Failed to apply $operation on image")

case class FailedToBlendImageDueToDifferentColorSpace(imageA: FreeImage, imageB: FreeImage)
  extends FreeImageException(
    s"Failed to blend image due to colorspace." +
    s"ImageA ${imageA.getColorSpaceType} and ImageB ${imageB.getColorSpaceType}"
  )