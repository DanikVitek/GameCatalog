package edu.mmsa.danikvitek.gamecatalog
package web.command

import javax.servlet.http.{ HttpServletRequest, HttpServletResponse }

object NoCommand extends Command {
    override def execute(req: HttpServletRequest, resp: HttpServletResponse): Unit = ()
}
