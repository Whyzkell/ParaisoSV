/* ==== Crear base de datos (si no existe) ==== */
IF DB_ID(N'ONGDonacionesDB') IS NULL
    CREATE DATABASE ONGDonacionesDB;
GO
USE ONGDonacionesDB;
GO

/* ==== Limpieza idempotente (para re-ejecutar el script) ==== */
DROP TABLE IF EXISTS dbo.Donaciones;
DROP TABLE IF EXISTS dbo.Alcancia;
DROP TABLE IF EXISTS dbo.PerroEnAdopcion;
DROP TABLE IF EXISTS dbo.Proyectos;
DROP TABLE IF EXISTS dbo.Usuario;
GO

/* =======================
   TABLA: Usuario
   Atributos: Id, Nombre, Correo, Password, Rol
   ======================= */
CREATE TABLE dbo.Usuario (
    Id           INT IDENTITY(1,1) PRIMARY KEY,
    Nombre       NVARCHAR(100)  NOT NULL,
    Correo       NVARCHAR(320)  NOT NULL UNIQUE,        -- único por email
    [Password]   NVARCHAR(255)  NOT NULL,               -- recomendado guardar hash
    Rol          NVARCHAR(20)   NOT NULL
        CONSTRAINT CK_Usuario_Rol CHECK (Rol IN (N'ADMIN', N'USER')),
    CreadoEn     DATETIME2(0)   NOT NULL CONSTRAINT DF_Usuario_CreadoEn DEFAULT SYSUTCDATETIME()
);
GO

/* =======================
   TABLA: Proyectos
   Atributos: Id, Tit, Descr, Img
   (no hay relaciones en el diagrama)
   ======================= */
CREATE TABLE dbo.Proyectos (
    Id           INT IDENTITY(1,1) PRIMARY KEY,
    Tit          NVARCHAR(150)  NOT NULL,
    Descr        NVARCHAR(MAX)  NULL,
    Img          NVARCHAR(500)  NULL,                   -- URL o ruta
    CreadoEn     DATETIME2(0)   NOT NULL CONSTRAINT DF_Proyectos_CreadoEn DEFAULT SYSUTCDATETIME()
);
GO

/* =======================
   TABLA: PerroEnAdopcion
   Atributos: Id, Nombre, Raza, Edad, Img, Descr
   (no hay relaciones en el diagrama)
   ======================= */
CREATE TABLE dbo.PerroEnAdopcion (
    Id           INT IDENTITY(1,1) PRIMARY KEY,
    Nombre       NVARCHAR(100)  NOT NULL,
    Raza         NVARCHAR(100)  NULL,
    Edad         INT            NULL
        CONSTRAINT CK_Perro_Edad CHECK (Edad IS NULL OR Edad >= 0),
    Img          NVARCHAR(500)  NULL,
    Descr        NVARCHAR(MAX)  NULL,
    CreadoEn     DATETIME2(0)   NOT NULL CONSTRAINT DF_Perro_CreadoEn DEFAULT SYSUTCDATETIME()
);
GO

/* =======================
   TABLA: Alcancia
   Atributos: Id, Descr, PrecioMeta, PrecioActual
   Relación 1:N con Donaciones (Alcancia tiene Donaciones)
   ======================= */
CREATE TABLE dbo.Alcancia (
    Id            INT IDENTITY(1,1) PRIMARY KEY,
    Descr         NVARCHAR(300)  NULL,
    PrecioMeta    DECIMAL(12,2)  NOT NULL
        CONSTRAINT CK_Alcancia_Meta CHECK (PrecioMeta >= 0),
    PrecioActual  DECIMAL(12,2)  NOT NULL CONSTRAINT DF_Alcancia_PrecioActual DEFAULT (0)
        CONSTRAINT CK_Alcancia_Actual CHECK (PrecioActual >= 0),
    CreadoEn      DATETIME2(0)   NOT NULL CONSTRAINT DF_Alcancia_CreadoEn DEFAULT SYSUTCDATETIME()
);
GO

/* =======================
   TABLA: Donaciones
   Atributos: Id, CantidadDonada, IdAlcancia, Fecha, IdUsuario
   Relaciones:
     - Usuario (1) —— (N) Donaciones
     - Alcancia (1) —— (N) Donaciones
   ======================= */
CREATE TABLE dbo.Donaciones (
    Id              INT IDENTITY(1,1) PRIMARY KEY,
    IdUsuario       INT            NOT NULL,
    IdAlcancia      INT            NOT NULL,
    CantidadDonada  DECIMAL(12,2)  NOT NULL
        CONSTRAINT CK_Don_Cantidad CHECK (CantidadDonada > 0),
    Fecha           DATETIME2(0)   NOT NULL CONSTRAINT DF_Don_Fecha DEFAULT SYSUTCDATETIME(),

    CONSTRAINT FK_Don_Usuario
        FOREIGN KEY (IdUsuario) REFERENCES dbo.Usuario(Id)
        ON DELETE CASCADE, -- si eliminas un usuario, se borran sus donaciones (ajústalo a tus reglas)

    CONSTRAINT FK_Don_Alcancia
        FOREIGN KEY (IdAlcancia) REFERENCES dbo.Alcancia(Id)
        ON DELETE CASCADE        -- si eliminas una alcancía, se borran sus donaciones (ajústalo si no quieres cascada)
);

-- Índices útiles para consultas por relaciones
CREATE INDEX IX_Donaciones_IdUsuario  ON dbo.Donaciones(IdUsuario);
CREATE INDEX IX_Donaciones_IdAlcancia ON dbo.Donaciones(IdAlcancia);
GO
