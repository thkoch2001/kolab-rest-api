package ro.koch.kolabrestapi

import org.fusesource.scalate.RenderContext._
import scala.xml.{NodeSeq, Elem, Attribute, Text, Null}

object TemplateHelpers {
  def foo (in:String) : String = {
    in + in + in
  }

  def microdata(prop:String)(body: => Unit) = {
    val cbody = captureNodeSeq(body)
    val newBody = cbody filter(_.label=="span") head match {
      case elem @ Elem(_, _, _, _, child @ _*) =>
         elem.asInstanceOf[Elem] %
              Attribute(None, "itemprop", Text(prop), Null) copy ( child = Text("new content"))
      case other => other
    }
    newBody
  }
}