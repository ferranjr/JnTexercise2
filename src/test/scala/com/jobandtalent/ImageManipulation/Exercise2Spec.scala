package com.jobandtalent.ImageManipulation

import java.awt.image.BufferedImage

import cats.data.State
import cats.free.Free
import cats.~>
import com.jobandtalent.ImageManipulation.FreeImageDSL._
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{FlatSpec, Matchers}

class Exercise2Spec
  extends FlatSpec
  with Matchers {

  val program: Free[ImageOps, Unit] =
    for {
      image <- loadImage("FOO")
      step1 <- flip(image, Horizontal)
      step2 <- flip(step1, Vertical)
      step3 <- rotate(step2, 180)
      image2 <- loadImage("BAR")
      step4 <- blend(step3, image2)
      _ <- saveImage("BAZ", step4)
    } yield ()

  "program" should "run sequentially" in {
    val res = program.foldMap(Exercise2Spec.LoggingInterpreter).run(List.empty[String])

    res.value._1.reverse should contain theSameElementsInOrderAs List(
      "Image Loaded",
      "Image Flipped",
      "Image Flipped",
      "Image Rotated",
      "Image Loaded",
      "Images Blended",
      "Image Saved"
    )
  }

}

object Exercise2Spec extends MockitoSugar {

  val bfi = mock[BufferedImage]
  val fullPath = "foo"
  val freeImage = FreeImage(fullPath, bfi)

  type StateLog[A] = State[List[String], A]

  object LoggingInterpreter
    extends (ImageOps ~> StateLog) {

    def apply[A](fa: ImageOps[A]): StateLog[A] =
      fa match {
        case LoadImage(_) =>
          State({ xs: List[String] => ("Image Loaded" :: xs, freeImage) })

        case SaveImage(destPath, image) =>
          State.modify("Image Saved" :: _)

        case Flip(img, direction) =>
          State({ xs: List[String] => ("Image Flipped" :: xs, freeImage) })

        case Rotate(image, rotation) =>
          State({ xs: List[String] => ("Image Rotated" :: xs, freeImage) })

        case Crop(image, x, y, w, h) =>
          State({ xs: List[String] => ("Image Cropped" :: xs, freeImage) })

        case Blend(imageA, imageB) =>
          State({ xs: List[String] => ("Images Blended" :: xs, freeImage) })
      }
  }

}
