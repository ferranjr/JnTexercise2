package com.jobandtalent.ImageManipulation.commons

import java.io.File
import javax.imageio.ImageIO

import com.jobandtalent.ImageManipulation.{FailedToLoadImage, FailedToSaveImage, FreeImage, FreeImageException}

object FileUtils {

  def loadImage(fullPath: String): Either[FreeImageException, FreeImage] = {
    try {
      val input = new File(fullPath)
      val bfi = ImageIO.read(input)
      Right(FreeImage(fullPath, bfi))
    } catch {
      case _: Throwable =>
        Left(FailedToLoadImage(fullPath))
    }
  }

  def saveImage(destPath: String, image: FreeImage): Either[FreeImageException, Unit] = {
    try {
      val output = new File(destPath)
      Right(ImageIO.write(image.bufferedImage, "jpg", output))
    } catch {
      case _: Throwable =>
        Left(FailedToSaveImage(destPath))
    }
  }
}
