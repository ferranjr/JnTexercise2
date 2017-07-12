package com.jobandtalent.ImageManipulation

import cats.free.Free
import com.jobandtalent.ImageManipulation.interpreters.InterpreterAwt


/**
  * Exercise2
  * ~~~~~
  * Here I am looking to split the definition of my dsl from the actual implementation,
  * to achieve this I am using a trendy/common pattern for Functional Programming, which
  * is the FreeMonad.
  *
  * Using free Monad we can define an algebra to define our DSL, together with some
  * lift methods to make it easier to use.
  *
  * On the ther hand, we can implement our Interpreters which we can use to achieve
  * different goals. As example I implemented a simple Identity interpreter using JAva Awt
  * but we could easily use other framework without the need of touching our business
  * logic.
  *
  */
object Exercise2
  extends App {

  import FreeImageDSL._

  val imageApath  = "src/main/resources/sampleA.jpg"
  val imageBpath  = "src/main/resources/sampleB.jpg"
  val destSrc     = "src/main/resources/result.jpg"

  /**
    * This is the definition of our program, we just enumerate the sequence of steps
    * that will be required.
    */
  val program: Free[ImageOps, Unit] =
    for {
      image   <- loadImage(imageApath)
      step1   <- flip(image, Horizontal)
      step2   <- flip(step1, Vertical)
      step3   <- rotate(step2, 180)
      image2  <- loadImage(imageBpath)
      step4   <- blend(step3, image2)
      _       <- saveImage(destSrc, step4)
    } yield ()


  /**
    * Running our program using a specific interpreter
    */
  try {
    program.foldMap(InterpreterAwt)
  } catch {
    case t: Throwable =>
      println(s"Error: Unable to execute the sequence of operations due to: \n${t.getMessage}")
  }

}

