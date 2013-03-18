package gitwatchdog {
  import org.freedesktop.dbus.DBusConnection
  import org.freedesktop.dbus.DBusSignal
  import org.freedesktop.dbus.DBusSigHandler
  import org.freedesktop.dbus.DBusSignal
  import org.freedesktop.dbus.Message
  import org.freedesktop.dbus.DBusInterface
  import org.kde.KNotify
  import org.freedesktop.dbus.UInt32
  import org.freedesktop.dbus.UInt64
  import org.freedesktop.dbus.Variant
  import org.freedesktop.Notifications

  object DBus {
    /**
     * implicits for adding signal handlers as function objects
     */
    implicit def functionToSigHandler[T <: DBusSignal](f: T => Unit) = {
      new DBusSigHandler[T] {
        def handle(signal: T) = f(signal)
      }
    }
    
    private val conn = DBusConnection.getConnection(DBusConnection.SESSION)

    def sendNotification(text: String) {
      val iface = conn.getRemoteObject("org.kde.knotify", "/Notify", classOf[KNotify])
      KNotify.send(iface, "New commits it watched repository", text)
    }

    def registerNotificationActionsListener(callback: (Int, Int) => Unit) {
      conn.addSigHandler(classOf[Notifications.ActionInvoked], 
          (s:Notifications.ActionInvoked) => 
            callback(s.messageId.intValue(), s.actionId.toInt)
      )
    }
  }
}

/**
 * DBus Signals and Service interfaces
 */
package org {
  package kde {
    import org.freedesktop.dbus.Variant
    import org.freedesktop.dbus.DBusInterface

    trait KNotify extends DBusInterface {
      def event(method: String, source: String, hints: Array[Variant[String]], title: String, text: String,
        pixmap: Array[Byte], actions: Array[String], timeout: Int, winId: Long): Int
    }

    object KNotify {
      def send(obj: KNotify, title: String, text: String) = {
        obj.event(
          method = "notification",
          source = "kde",
          hints = Array[Variant[String]](),
          title = title,
          text = text,
          pixmap = Array[Byte](0, 0, 0, 0),
          actions = Array[String]("Accept"),
          timeout = 0,
          winId = 0)
      }
    }
  }
}

package org {
  package freedesktop {
    import org.freedesktop.dbus.DBusInterface
    import org.freedesktop.dbus.DBusSignal
    import org.freedesktop.dbus.UInt32

    trait Notifications extends DBusInterface

    object Notifications {
      class ActionInvoked(path: String, id: UInt32, action: String) extends DBusSignal(path: String, "") {
        val messageId = id
        val actionId = action
      }
    }
  }
}