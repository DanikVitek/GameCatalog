package edu.mmsa.danikvitek.gamecatalog
package web.command

import dto.{ ErrorDto, errorDtoToJson }
import exception.{ IllegalContentTypeException as ICTE, InvalidDtoException as IRDE, InvalidEmailException as IEE, UserAlreadyRegisteredException as UARE }

import com.typesafe.scalalogging.Logger
import little.json.Implicits.jsonValueToJsonObject
import little.json.Json

import javax.servlet.http.{ HttpServletRequest, HttpServletResponse }

case class ErrorCommand(th: Throwable) extends Command {
    private final lazy val LOGGER = Logger(this.getClass)

    override def execute(req: HttpServletRequest, resp: HttpServletResponse): Unit = {
        val json = th match
            case e: ICTE => ex400(e, resp)
            case e: IRDE => ex400(e, resp)
            case e: UARE => ex400(e, resp)
            case e: IEE => ex400(e, resp)
            case _ => ex500(th, resp)
        LOGGER info s"Sent the error dto: $json"
    }

    private def ex400(e: RuntimeException, resp: HttpServletResponse): String = {
        resp.setStatus(400)
        resp.setContentType("application/json")
        val json = Json.toPrettyPrint(Json.toJson(ErrorDto(400, e.getMessage)))
        resp.getWriter.write(json)
        json
    }

    private def ex500(th: Throwable, resp: HttpServletResponse): String = {
        resp.setStatus(500)
        resp.setContentType("application/json")
        val json = Json.toPrettyPrint(Json.toJson(ErrorDto(500, th.getMessage)))
        resp.getWriter.write(json)
        json
    }
}
