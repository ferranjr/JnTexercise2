package com.jobandtalent.ImageManipulation

import java.awt.Graphics2D
import java.awt.geom.AffineTransform
import java.awt.image.{AffineTransformOp, BufferedImage}
import java.io.File
import javax.imageio.ImageIO

import cats.free.Free
import cats.~>

case class Img(
  fullPath: String
)

sealed trait ImageOps[A]

case class Flip(image: Img) extends ImageOps[Img]
case class Rotate(image: Img, rotation: Int) extends ImageOps[Img]
case class Crop(image: Img, x0: Int, y0: Int, x1: Int, y1: Int) extends ImageOps[Img]
case class Blend(imageA: Img, imageB: Img) extends ImageOps[Img]


object FreeImageDSL {
  def flip(image: Img): Free[ImageOps, Img] = Free.liftF(Flip(image))

  def rotate(image: Img, rotation: Int): Free[ImageOps, Img] = Free.liftF(Rotate(image, rotation))

  def crop(image: Img, x0: Int, y0: Int, x1: Int, y1: Int): Free[ImageOps, Img] = Free.liftF(Crop(image, x0, y0, x1, y1))

  def blend(imageA: Img, imageB: Img): Free[ImageOps, Img] = Free.liftF(Blend(imageA, imageB))
}

object Interpreters {
  type Id[A] = A

  object InterpreterAwt extends (ImageOps ~> Id) {

    private[this] def applyTranformation(bfi: BufferedImage, transform: AffineTransform): BufferedImage = {

      val target = new BufferedImage(bfi.getHeight, bfi.getWidth, bfi.getType)
      val g2d: Graphics2D = target.createGraphics()

      g2d.drawImage(bfi, 0, 0, null)
      g2d.dispose()

      target
    }


    private[this] def transformFile(image: Img): AffineTransform => Id[Img] = transform => {

      val input = new File(image.fullPath)

      val bfi = applyTranformation(ImageIO.read(input), transform)

      try {
        if(ImageIO.write(bfi, "jpg", new File("foo.jpg"))) image
        else throw new Exception("didnt write file properly")
      } catch {
        case t: Throwable =>
          throw new Exception(s"error fliping image: ${t.getMessage}")
      }
    }

    private[this] def flipImage(image: Img): Img = {
      val transform = new AffineTransform()
      transform.scale(-1, -1)

      transformFile(image)(transform)
    }

    private[this] def rotateImage(image: Img, rotation: Int): Img = {
      val transform = new AffineTransform()
      transform.rotate(rotation)

      transformFile(image)(transform)
    }

    private[this] def cropImage(image: Img, x0: Int, y0: Int, x1: Int, y1: Int): Img = {
      println("Cropped")
      image
    }

    private[this] def blendImages(imageA: Img, imageB: Img): Img = {
      println("Blended")
      imageA
    }

    def apply[A](fa: ImageOps[A]): A =
      fa match {
        case Flip(img) => flipImage(img)
        case Rotate(image, rotation) => rotateImage(image, rotation)
        case Crop(image, x0, y0, x1, y1) => cropImage(image, x0, y0, x1, y1)
        case Blend(imageA, imageB) => blendImages(imageA, imageB)
      }
  }

}

object Exercise2
  extends App {

  import FreeImageDSL._

  val program: Img => Free[ImageOps, Img] = {
    image =>
      for {
        step1 <- rotate(image, 90)
        step2 <- flip(step1)
        step3 <- blend(step1, step2)
      } yield step3
  }

  program(Img("src/main/resources/racoon.jpg")).foldMap(Interpreters.InterpreterAwt)
}

