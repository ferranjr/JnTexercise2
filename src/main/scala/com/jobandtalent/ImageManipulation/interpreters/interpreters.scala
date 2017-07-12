package com.jobandtalent.ImageManipulation

import java.awt.{AlphaComposite, Graphics2D}
import java.awt.geom.AffineTransform
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

import cats.~>

package object interpreters {

  type Id[A] = A

  /**
    * This is a pretty dummy interpreter to execute the ImageOps,
    */
  object InterpreterAwt
    extends (ImageOps ~> Id) {

    private[this] def applyTranformation(bfi: BufferedImage, transform: AffineTransform): BufferedImage = {

      val target = new BufferedImage(bfi.getWidth, bfi.getHeight, bfi.getType)
      val g2d: Graphics2D = target.createGraphics()

      g2d.drawImage(bfi, transform, null)
      g2d.dispose()

      target
    }

    private[this] def flipImage(image: FreeImage, axis: Direction): FreeImage = {
      try {
        val transform = new AffineTransform()

        val width = image.getWidth
        val height = image.getHeight

        transform.translate(width / 2, height / 2)
        axis match {
          case Horizontal =>
            transform.scale(-1, 1)
          case Vertical =>
            transform.scale(1, -1)
        }
        transform.translate(-width / 2, -height / 2)

        image.copy(bufferedImage = applyTranformation(image.bufferedImage, transform))
      } catch {
        case _: Throwable =>
          throw FailedOperatingOverImage("flipImage")
      }
    }

    private[this] def rotateImage(image: FreeImage, rotation: Int): FreeImage = {
      try {
        val transform = new AffineTransform()

        val width = image.getWidth
        val height = image.getHeight

        transform.translate(width/2, height/2)
        transform.rotate(Math.toRadians(rotation))
        transform.translate(-width/2, -height/2)

        image.copy(bufferedImage = applyTranformation(image.bufferedImage, transform))
      } catch {
        case _: Throwable =>
          throw FailedOperatingOverImage("flipImage")
      }
    }

    private[this] def cropImage(image: FreeImage, x: Int, y: Int, w: Int, h: Int): FreeImage = {
      try {
        val bfi = image.bufferedImage
        image.copy(bufferedImage = bfi.getSubimage(x, y, w, h))
      } catch {
        case _: Throwable =>
          throw FailedOperatingOverImage("cropImage")
      }
    }

    private[this] def blendImages(imageA: FreeImage, imageB: FreeImage): FreeImage = {
      val bfiA = imageA.bufferedImage
      val bfiB = imageB.bufferedImage

      if(imageA.getColorSpaceType == imageB.getColorSpaceType) {
        try {
          val g2d: Graphics2D = bfiA.createGraphics()

          g2d.setComposite(AlphaComposite.SrcOver.derive(0.5f))
          g2d.drawImage(bfiB, (bfiA.getWidth - bfiB.getWidth) / 2, (bfiA.getHeight - bfiB.getHeight) / 2, null)
          g2d.dispose()

          imageA
        } catch {
          case _: Throwable =>
            throw FailedOperatingOverImage("blendImages")
        }
      } else {
        throw FailedToBlendImageDueToDifferentColorSpace(imageA, imageB)
      }
    }

    private[this] def loadImage(fullPath: String): FreeImage = {
      try {
        val input = new File(fullPath)
        val bfi = ImageIO.read(input)
        FreeImage(fullPath, bfi)
      } catch {
        case _: Throwable =>
          throw FailedToLoadImage(fullPath)
      }
    }

    private[this] def saveImage(destPath: String, image: FreeImage): Unit = {
      try {
        val output = new File(destPath)
        ImageIO.write(image.bufferedImage, "jpg", output)
      } catch {
        case _: Throwable =>
          throw FailedToSaveImage(destPath)
      }
    }

    /**
      * This is the main implementation of the interpreter, where we basically define
      * what to do for each one of the AST components defined on our little DSL.
      * @param fa
      * @tparam A
      * @return
      */
    def apply[A](fa: ImageOps[A]): A = fa match {
      case LoadImage(fullPath) =>
        loadImage(fullPath)

      case SaveImage(destPath, image) =>
        saveImage(destPath, image)

      case Flip(img, direction) =>
        flipImage(img, direction)

      case Rotate(image, rotation) =>
        rotateImage(image, rotation)

      case Crop(image, x, y, w, h) =>
        cropImage(image, x, y, w, h)

      case Blend(imageA, imageB) =>
        blendImages(imageA, imageB)
    }
  }

}