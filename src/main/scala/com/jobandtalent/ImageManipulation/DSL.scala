package com.jobandtalent.ImageManipulation

import cats.free.Free


/**
  * ImageOps
  *
  * AST with the operations that our Library DSL allows to do
  * @tparam A Type of the return of the operation represented by the case class
  */
sealed trait ImageOps[A]

case class LoadImage(fullPath: String) extends ImageOps[FreeImage]
case class SaveImage(destPath: String, image: FreeImage) extends ImageOps[Unit]
case class Flip(image: FreeImage, axis: Direction) extends ImageOps[FreeImage]
case class Rotate(image: FreeImage, rotation: Int) extends ImageOps[FreeImage]
case class Crop(image: FreeImage, x0: Int, y0: Int, x1: Int, y1: Int) extends ImageOps[FreeImage]
case class Blend(imageA: FreeImage, imageB: FreeImage) extends ImageOps[FreeImage]


/**
  * Small set of helpers to use as simpler DSL to create our
  * instances of the ADT lifted within the FreeMonad
  */
object FreeImageDSL {

  def loadImage(fullPath: String): Free[ImageOps, FreeImage] =
    Free.liftF(LoadImage(fullPath))

  def saveImage(destPath: String, image: FreeImage): Free[ImageOps, Unit] =
    Free.liftF(SaveImage(destPath, image))

  def flip(image: FreeImage, axis: Direction = Horizontal): Free[ImageOps, FreeImage] =
    Free.liftF(Flip(image, axis))

  def rotate(image: FreeImage, rotation: Int): Free[ImageOps, FreeImage] =
    Free.liftF(Rotate(image, rotation))

  def crop(image: FreeImage, x: Int, y: Int, w: Int, h: Int): Free[ImageOps, FreeImage] =
    Free.liftF(Crop(image, x, y, w, h))

  def blend(imageA: FreeImage, imageB: FreeImage): Free[ImageOps, FreeImage] =
    Free.liftF(Blend(imageA, imageB))
}