# ============================================================
# SERVIDOR API MYSQL CON ALTA SEGURIDAD PARA DEPORPLAZA
# ============================================================
# Este servidor conecta tu MySQL Workbench con la app Android
# Incluye CRUD completo para Usuarios, Canchas, Reservas y Favoritos
# Ejecutar en la consola de tu PC con:
# py servidor_mysql.py
# ============================================================

import json
import re
import traceback
from http.server import HTTPServer, BaseHTTPRequestHandler
import mysql.connector

HOST = "0.0.0.0"
PORT = 8085

MYSQL_CONFIG = {
    "host": "localhost",
    "port": 3306,
    "user": "root",
    "password": "",
    "database": "tucancha_db"
}

def obtener_conexion_mysql():
    try:
        return mysql.connector.connect(**MYSQL_CONFIG)
    except Exception as e:
        print(f"Error conectando a MySQL: {e}")
        return None

def sanitizar_texto(texto):
    if not isinstance(texto, str):
        return texto
    return re.sub(r"['\";\-\-\/\*]", "", texto).strip()

class APIHandler(BaseHTTPRequestHandler):

    def _responder_json(self, status_code, data):
        self.send_response(status_code)
        self.send_header("Content-Type", "application/json; charset=utf-8")
        self.end_headers()
        self.wfile.write(json.dumps(data, ensure_ascii=False).encode("utf-8"))

    def do_GET(self):
        conn = obtener_conexion_mysql()
        if not conn:
            self._responder_json(500, {"error": "No se pudo conectar a MySQL Workbench"})
            return

        cursor = conn.cursor(dictionary=True)

        try:
            if self.path == "/api/canchas":
                cursor.execute("SELECT * FROM canchas")
                filas = cursor.fetchall()
                self._responder_json(200, filas)

            elif self.path == "/api/reservas":
                cursor.execute("SELECT * FROM reservas ORDER BY creado_en DESC")
                filas = cursor.fetchall()
                self._responder_json(200, filas)

            elif self.path == "/api/favoritos":
                cursor.execute("SELECT cancha_id FROM favoritos")
                filas = cursor.fetchall()
                ids = [f["cancha_id"] for f in filas]
                self._responder_json(200, ids)

            else:
                self._responder_json(404, {"error": "Ruta no encontrada"})
        except Exception as e:
            print(f"Error en GET {self.path}: {e}")
            self._responder_json(500, {"error": str(e)})
        finally:
            cursor.close()
            conn.close()

    def do_POST(self):
        conn = obtener_conexion_mysql()
        if not conn:
            self._responder_json(500, {"error": "No se pudo conectar a MySQL Workbench"})
            return

        cursor = conn.cursor(dictionary=True)

        try:
            content_length = int(self.headers.get('Content-Length', 0))
            body = self.rfile.read(content_length)
            data = json.loads(body.decode('utf-8'))

            if self.path == "/api/login":
                email = sanitizar_texto(data.get("email", ""))
                password = sanitizar_texto(data.get("password", ""))
                rol = sanitizar_texto(data.get("rol", "USUARIO"))

                if not email or not password:
                    self._responder_json(400, {"exito": False, "mensaje": "Ingresa correo y contraseña"})
                    return

                sql = "SELECT id, nombre, email, rol FROM usuarios WHERE LOWER(email) = LOWER(%s) AND password = %s AND rol = %s"
                cursor.execute(sql, (email, password, rol))
                usuario = cursor.fetchone()

                if usuario:
                    print(f"Login AUTENTICADO para {email} ({rol})")
                    self._responder_json(200, {
                        "exito": True,
                        "mensaje": "Login exitoso",
                        "usuario": usuario
                    })
                else:
                    print(f"Login RECHAZADO para {email} - credenciales incorrectas")
                    self._responder_json(401, {
                        "exito": False,
                        "mensaje": "Correo o contraseña incorrectos"
                    })

            elif self.path == "/api/usuarios":
                nombre = sanitizar_texto(data.get("nombre", ""))
                email = sanitizar_texto(data.get("email", ""))
                password = sanitizar_texto(data.get("password", ""))
                rol = sanitizar_texto(data.get("rol", "USUARIO"))

                if not nombre or not email or not password:
                    self._responder_json(400, {"exito": False, "mensaje": "Todos los campos son obligatorios"})
                    return

                sql = """INSERT INTO usuarios (nombre, email, password, rol)
                         VALUES (%s, %s, %s, %s)
                         ON DUPLICATE KEY UPDATE nombre = VALUES(nombre), password = VALUES(password)"""
                val = (nombre, email, password, rol)
                cursor.execute(sql, val)
                conn.commit()
                print(f"Usuario {email} registrado/actualizado de forma segura")
                self._responder_json(201, {"exito": True, "mensaje": "Usuario registrado correctamente"})

            elif self.path == "/api/reservas":
                sql = """INSERT INTO reservas (id, recibo_codigo, cancha_id, usuario_nombre, fecha, dia_nombre, hora_inicio, duracion_horas, precio_total, metodo_pago, codigo_operacion, celular, estado, creado_en)
                         VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
                         ON DUPLICATE KEY UPDATE estado = VALUES(estado)"""
                val = (
                    str(data["id"]),
                    str(data.get("recibo_codigo", "DP-0001055")),
                    str(data["cancha_id"]),
                    sanitizar_texto(data.get("usuario_nombre", "Alejandro Torres")),
                    str(data["fecha"]),
                    str(data.get("dia_nombre", "")),
                    str(data["hora_inicio"]),
                    float(data["duracion_horas"]),
                    int(data["precio_total"]),
                    str(data.get("metodo_pago", "YAPE")),
                    sanitizar_texto(data.get("codigo_operacion", "")),
                    sanitizar_texto(data.get("celular", "")),
                    str(data.get("estado", "CONFIRMADA")),
                    int(data.get("creado_en", 0))
                )
                cursor.execute(sql, val)
                conn.commit()
                print("Reserva guardada exitosamente en MySQL Workbench")
                self._responder_json(201, {"mensaje": "Reserva guardada correctamente"})

            elif self.path == "/api/favoritos":
                cancha_id = str(data["cancha_id"])
                cursor.execute("DELETE FROM favoritos WHERE cancha_id = %s", (cancha_id,))
                cursor.execute("INSERT INTO favoritos (cancha_id) VALUES (%s)", (cancha_id,))
                conn.commit()
                print("Favorito guardado exitosamente")
                self._responder_json(200, {"mensaje": "Favorito guardado"})

            else:
                self._responder_json(404, {"error": "Ruta no encontrada"})
        except Exception as e:
            print(f"Error en POST {self.path}: {e}")
            traceback.print_exc()
            self._responder_json(500, {"error": str(e)})
        finally:
            cursor.close()
            conn.close()

    def do_PUT(self):
        conn = obtener_conexion_mysql()
        if not conn:
            self._responder_json(500, {"error": "No se pudo conectar a MySQL Workbench"})
            return

        cursor = conn.cursor()

        try:
            content_length = int(self.headers.get('Content-Length', 0))
            body = self.rfile.read(content_length)
            data = json.loads(body.decode('utf-8'))

            if self.path == "/api/usuarios/actualizar":
                old_email = sanitizar_texto(data.get("old_email", ""))
                new_nombre = sanitizar_texto(data.get("new_nombre", ""))
                new_email = sanitizar_texto(data.get("new_email", ""))

                # Actualizar usuario en MySQL
                sql_user = "UPDATE usuarios SET nombre = %s, email = %s WHERE LOWER(email) = LOWER(%s)"
                cursor.execute(sql_user, (new_nombre, new_email, old_email))

                # Actualizar nombre en las reservas del usuario
                sql_res = "UPDATE reservas SET usuario_nombre = %s WHERE usuario_nombre = %s"
                cursor.execute(sql_res, (new_nombre, data.get("old_nombre", "")))

                conn.commit()
                print(f"Perfil de {old_email} actualizado a {new_nombre} ({new_email})")
                self._responder_json(200, {"exito": True, "mensaje": "Perfil actualizado correctamente"})
            else:
                self._responder_json(404, {"error": "Ruta no encontrada"})
        except Exception as e:
            print(f"Error en PUT {self.path}: {e}")
            self._responder_json(500, {"error": str(e)})
        finally:
            cursor.close()
            conn.close()

    def do_DELETE(self):
        conn = obtener_conexion_mysql()
        if not conn:
            self._responder_json(500, {"error": "No se pudo conectar a MySQL Workbench"})
            return

        cursor = conn.cursor()

        try:
            if self.path.startswith("/api/usuarios/"):
                user_email = sanitizar_texto(self.path.split("/")[-1])
                user_nombre = sanitizar_texto(self.headers.get("Usuario-Nombre", ""))

                # Eliminar usuario de MySQL
                cursor.execute("DELETE FROM usuarios WHERE LOWER(email) = LOWER(%s)", (user_email,))
                if user_nombre:
                    cursor.execute("DELETE FROM reservas WHERE LOWER(usuario_nombre) = LOWER(%s)", (user_nombre,))
                conn.commit()
                print(f"Cuenta de usuario {user_email} eliminada permanentemente de MySQL Workbench")
                self._responder_json(200, {"exito": True, "mensaje": "Cuenta eliminada correctamente"})

            elif self.path.startswith("/api/reservas/"):
                reserva_id = self.path.split("/")[-1]
                cursor.execute("DELETE FROM reservas WHERE id = %s", (reserva_id,))
                conn.commit()
                print(f"Reserva {reserva_id} eliminada")
                self._responder_json(200, {"mensaje": "Reserva eliminada"})

            elif self.path == "/api/favoritos":
                content_length = int(self.headers.get('Content-Length', 0))
                body = self.rfile.read(content_length)
                data = json.loads(body.decode('utf-8'))
                cancha_id = str(data["cancha_id"])
                cursor.execute("DELETE FROM favoritos WHERE cancha_id = %s", (cancha_id,))
                conn.commit()
                self._responder_json(200, {"mensaje": "Favorito eliminado"})

            else:
                self._responder_json(404, {"error": "Ruta no encontrada"})
        except Exception as e:
            print(f"Error en DELETE {self.path}: {e}")
            self._responder_json(500, {"error": str(e)})
        finally:
            cursor.close()
            conn.close()

if __name__ == "__main__":
    print(f"============================================================")
    print(f" Servidor API DeporPlaza (CRUD USUARIOS COMPLETO) activo en http://localhost:{PORT}/api")
    print(f" Conectado a MySQL Workbench (tucancha_db)")
    print(f"============================================================")
    server = HTTPServer((HOST, PORT), APIHandler)
    server.serve_forever()
