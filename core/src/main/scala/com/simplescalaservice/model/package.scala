package com.simplescalaservice

import eu.timepit.refined.types.string.NonEmptyString

package object model {

  type Greet = NonEmptyString
  val Greet = NonEmptyString

}
