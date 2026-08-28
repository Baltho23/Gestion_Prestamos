-- Semilla de usuarios (solo perfil por defecto / H2).
-- Contraseña de todos: 123456  (hash BCrypt generado con el BCryptPasswordEncoder del proyecto)

INSERT INTO usuarios (email, password, nombre, rol, fecha_creacion) VALUES
  ('laura.martinez@bancolineal.com', '$2a$10$B530QuyXoaQ2XEvDC/xy1emjA5AXPJxzt6J9fPbg7IWMkeq09oAEq', 'Laura Martínez',  'ADMIN', CURRENT_TIMESTAMP),
  ('carlos.ramirez@bancolineal.com', '$2a$10$B530QuyXoaQ2XEvDC/xy1emjA5AXPJxzt6J9fPbg7IWMkeq09oAEq', 'Carlos Ramírez',  'ADMIN', CURRENT_TIMESTAMP),
  ('ana.torres@gmail.com',           '$2a$10$B530QuyXoaQ2XEvDC/xy1emjA5AXPJxzt6J9fPbg7IWMkeq09oAEq', 'Ana Torres',      'USER',  CURRENT_TIMESTAMP),
  ('diego.fernandez@gmail.com',      '$2a$10$B530QuyXoaQ2XEvDC/xy1emjA5AXPJxzt6J9fPbg7IWMkeq09oAEq', 'Diego Fernández', 'USER',  CURRENT_TIMESTAMP),
  ('maria.gonzalez@outlook.com',     '$2a$10$B530QuyXoaQ2XEvDC/xy1emjA5AXPJxzt6J9fPbg7IWMkeq09oAEq', 'María González',  'USER',  CURRENT_TIMESTAMP);
