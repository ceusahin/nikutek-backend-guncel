# Nikutek Backend

Nikutek ve Nikuni Pompa web siteleri için geliştirilmiş, tek bir uygulama içinde iki farklı siteyi yöneten **Spring Boot** tabanlı REST API backend projesidir.

---

## 📋 İçindekiler

- [Genel Bakış](#-genel-bakış)
- [Kullanılan Teknolojiler](#-kullanılan-teknolojiler)
- [Proje Yapısı](#-proje-yapısı)
- [Veritabanı Mimarisi](#-veritabanı-mimarisi)
- [API Yapısı](#-api-yapısı)
- [Kurulum ve Çalıştırma](#-kurulum-ve-çalıştırma)
- [Ortam Değişkenleri](#-ortam-değişkenleri)
- [Docker](#-docker)

---

## 🎯 Genel Bakış

Bu proje, **Nikutek** (ana marka) ve **Nikuni Pompa** (alt marka) web sitelerinin içerik ve verilerini yönetmek için tek bir backend sunar. İki site ayrı veritabanı şemalarında (`nikutek` ve `nikunipompa`) tutulur; API yolları `/api/nikutek/...` ve `/api/nikuni-pompa/...` ile ayrılır.

**Temel özellikler:**

- Çok dilli içerik (dil tabloları ve çeviri entity'leri)
- Blog, ürün, teknoloji, referanslar, deneyim (experience) yönetimi
- SEO ayarları, sayfa bazlı SEO, sosyal medya ayarları
- İletişim formu mesajları, harita ayarları
- Logo, favicon, ana sayfa hero, footer menü yönetimi
- Stok takibi ve stok logları
- Admin kullanıcı yönetimi ve basit token tabanlı kimlik doğrulama
- Cloudinary ile medya yükleme, yerel PDF dosya sunumu
- CORS yapılandırması (localhost, nikutek.com.tr, barissmutllu.com vb.)

---

## 🛠 Kullanılan Teknolojiler

| Kategori | Teknoloji |
|----------|-----------|
| **Framework** | Spring Boot 3.5.6 |
| **Java** | 17 |
| **Güvenlik** | Spring Security (HTTP Basic, BCrypt) |
| **Veritabanı** | PostgreSQL (MariaDB client dependency mevcut) |
| **ORM** | Spring Data JPA / Hibernate |
| **Medya** | Cloudinary (cloudinary-core, cloudinary-http44) |
| **Yapı** | Maven |
| **Yardımcı** | Lombok, dotenv-java |
| **Test** | Spring Boot Test, Spring Security Test |

---

## 📁 Proje Yapısı

```
src/main/java/com/example/nikutek/
├── NikutekApplication.java          # Uygulama giriş noktası
├── config/
│   ├── CloudinaryConfig.java       # Cloudinary bean yapılandırması
│   └── SecurityConfig.java         # CORS, CSRF, HTTP Basic, PasswordEncoder
├── controller/
│   ├── AuthController.java         # /api/nikutek/auth (login)
│   ├── AdminUserController.java   # Admin kullanıcı CRUD
│   ├── AdminLogController.java    # Admin işlem logları
│   ├── BlogPostController.java    # Nikutek blog
│   ├── NikuniPompaBlogPostController.java
│   ├── ProductController.java      # Nikutek ürünler
│   ├── NikuniPompaProductController.java
│   ├── TechnologyController.java
│   ├── NikuniPompaTechnologyController.java
│   ├── AboutUsController.java / NikuniPompaAboutUsController.java
│   ├── CompanyInfoController.java / NikuniPompaCompanyInfoController.java
│   ├── ContactInfoController.java / NikuniPompaContactInfoController.java
│   ├── ContactMessageController.java / NikuniPompaContactMessageController.java
│   ├── ExperienceController.java / NikuniPompaExperienceController.java
│   ├── FaviconController, LogoController, MainHeroController
│   ├── FooterMenuController, FooterMenuItemController
│   ├── MapSettingsController, LanguageController
│   ├── PageSeoSettingsController, SeoSettingsController
│   ├── SocialMediaSettingsController
│   ├── ReferencesController / NikuniPompaReferencesController
│   ├── StockController / NikuniPompaStockController
│   ├── StatsController / NikuniPompaStatsController
│   └── ...
├── dto/                            # Data Transfer Object'ler
├── entity/                         # JPA Entity'ler (nikutek / nikunipompa şemaları)
├── enums/
│   ├── BlogPostType.java          # images, video
│   └── InfoType.java
├── exception/
│   ├── GlobalExceptionHandler.java
│   ├── NikutekErrorResponse.java
│   ├── NikutekException.java
│   └── NotFoundException.java
├── repository/                     # Spring Data JPA Repository'ler
├── service/                        # İş mantığı (interface + impl)
└── utils/
    └── SlugGenerator.java
```

---

## 🗄 Veritabanı Mimarisi

- **PostgreSQL** kullanılır; sürücü `org.postgresql.Driver`.
- **İki şema:**
  - **`nikutek`** — Nikutek sitesi (blog_posts, product, technology, about_section, contact_info, admin_user, vb.)
  - **`nikunipompa`** — Nikuni Pompa sitesi (aynı kavramlar, ayrı tablolar)
- **JPA:** `spring.jpa.hibernate.ddl-auto=update` ile şema güncellenir.
- **Çok dillilik:** Çeviri tabloları kullanılır (örn. `blog_post_translation`, `product_translation`, `technology_translation`, `contact_info_translation`, `experience_translation`).
- **İlişkiler:** Ürün hiyerarşisi (parent/children), blog–görsel, referans endüstri–şirket vb.

**Örnek entity–şema eşlemesi:**

| Şema | Örnek tablolar |
|------|-----------------|
| `nikutek` | blog_posts, product, technology, about_section, contact_info, admin_user, main_hero, footer_menu, seo_settings, stock, visits |
| `nikunipompa` | blog_posts, product, product_feature, product_catalog, technology, about_section, contact_info, main_hero, seo_settings, stock, visits |

**Not:** `database_migration_product_feature_frequency.sql` ile `product_feature` tablosuna `frequency` kolonu eklenebilir.

---

## 🔌 API Yapısı

Tüm API'ler REST JSON tabanlıdır.

### Nikutek (`/api/nikutek/...`)

| Yol | Açıklama |
|-----|----------|
| `POST /api/nikutek/auth/login` | Admin girişi (username, password → token/başarı mesajı) |
| `/api/nikutek/admin` | Admin kullanıcı ve log yönetimi |
| `/api/nikutek/blog` | Blog yazıları (CRUD, çoklu dil, görsel yükleme) |
| `/api/nikutek/products` | Ürünler |
| `/api/nikutek/technologies` | Teknolojiler |
| `/api/nikutek/about-us` | Hakkımızda |
| `/api/nikutek/company-info` | Şirket bilgisi |
| `/api/nikutek/contact-info` | İletişim bilgisi |
| `/api/nikutek/contact` | İletişim formu mesajları |
| `/api/nikutek/experience` | Deneyim içerikleri |
| `/api/nikutek/main-hero` | Ana sayfa hero |
| `/api/nikutek/logo`, `/api/nikutek/favicon` | Logo ve favicon |
| `/api/nikutek/footer-menu`, `/api/nikutek/footer-menu-item` | Footer menü |
| `/api/nikutek/map-settings` | Harita ayarları |
| `/api/nikutek/languages` | Diller |
| `/api/nikutek/seo`, `/api/nikutek/page-seo` | SEO ayarları |
| `/api/nikutek/social-media` | Sosyal medya ayarları |
| `/api/nikutek/references` | Referanslar |
| `/api/nikutek/stocks` | Stoklar |
| `/api/nikutek/stats` | İstatistikler |

### Nikuni Pompa (`/api/nikuni-pompa/...`)

Aynı modüller Nikuni Pompa için tekrarlanır; örneğin:

- `/api/nikuni-pompa/products` (slug ile getirme, PDF serve, Cloudinary upload)
- `/api/nikuni-pompa/technologies`
- `/api/nikuni-pompa/blog`, `/api/nikuni-pompa/about-us`, `/api/nikuni-pompa/contact-info`
- `/api/nikuni-pompa/main-hero`, `/api/nikuni-pompa/logo`, `/api/nikuni-pompa/favicon`
- `/api/nikuni-pompa/seo`, `/api/nikuni-pompa/page-seo`, `/api/nikuni-pompa/social-media`
- `/api/nikuni-pompa/references`, `/api/nikuni-pompa/stocks`, `/api/nikuni-pompa/stats`
- `/api/nikuni-pompa/map-settings`, `/api/nikuni-pompa/experience`
- `/api/nikuni-pompa/footer-menu`, `/api/nikuni-pompa/footer-menu-item`

Ürün ve teknoloji için slug tabanlı endpoint örneği: `GET /api/nikuni-pompa/products/slug/{slug}?lang=tr`.

---

## ⚙ Kurulum ve Çalıştırma

### Gereksinimler

- **JDK 17**
- **Maven 3.6+**
- **PostgreSQL** (nikutek ve nikunipompa şemaları oluşturulmuş olmalı; `ddl-auto=update` ile tablolar uygulama tarafından güncellenir)

### Adımlar

1. Projeyi klonlayın.
2. Veritabanı şemalarını oluşturun (gerekirse):
   ```sql
   CREATE SCHEMA IF NOT EXISTS nikutek;
   CREATE SCHEMA IF NOT EXISTS nikunipompa;
   ```
3. Ortam değişkenlerini ayarlayın (aşağıdaki bölüme bakın).
4. Uygulamayı çalıştırın:
   ```bash
   ./mvnw spring-boot:run
   ```
   Windows:
   ```cmd
   mvnw.cmd spring-boot:run
   ```
5. Varsayılan olarak uygulama **8080** portunda ayağa kalkar.

---

## 🔐 Ortam Değişkenleri

`application.properties` aşağıdaki değişkenleri kullanır; değerleri ortamdan veya `.env` benzeri bir yöntemle (dotenv-java ile) sağlayabilirsiniz.

| Değişken | Açıklama |
|----------|----------|
| `SPRING_DATASOURCE_URL` | PostgreSQL JDBC URL (örn. `jdbc:postgresql://localhost:5432/db_adi`) |
| `SPRING_DATASOURCE_USERNAME` | Veritabanı kullanıcı adı |
| `SPRING_DATASOURCE_PASSWORD` | Veritabanı şifresi |

**Örnek:**

```properties
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/nikutek_db
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=your_password
```

Cloudinary ayarları şu an `CloudinaryConfig` içinde sabit; production için ortam değişkenlerine taşınması önerilir.

---

## 🐳 Docker

Proje çok aşamalı bir Dockerfile ile derlenip çalıştırılabilir:

```bash
docker build -t nikutek-backend .
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/nikutek_db \
  -e SPRING_DATASOURCE_USERNAME=postgres \
  -e SPRING_DATASOURCE_PASSWORD=your_password \
  nikutek-backend
```

- **Build aşaması:** Maven 3.9.6 + Eclipse Temurin 17 ile `mvn clean package -DskipTests`.
- **Çalışma aşaması:** Eclipse Temurin 17 JDK ile `app.jar` çalıştırılır; port **8080** expose edilir.

---

## 📄 Dosya Yükleme ve Medya

- **Cloudinary:** Görsel yüklemeleri Cloudinary’ye gönderilir (`CloudinaryConfig`, ilgili service’ler).
- **Yerel dosya:** PDF vb. dosyalar `file.upload.dir`, `file.upload.products` gibi ayarlarla yerel dizine yazılır; Nikuni Pompa ürün controller’ında `/api/nikuni-pompa/products/files/{fileName}` ile sunulur.
- **Multipart:** Maksimum dosya boyutu 100MB, istek boyutu 100MB olacak şekilde yapılandırılmıştır.

---

## 🔒 Güvenlik

- **CSRF:** Devre dışı (API kullanımı için).
- **CORS:** Belirli origin’ler ve `https://*.nikutek.com.tr` pattern’i ile sınırlı; credentials desteklenir.
- **Kimlik doğrulama:** HTTP Basic + `/api/nikutek/auth/login` ile admin girişi; şifreler BCrypt ile hash’lenir.
- Tüm istekler şu an `permitAll()` ile açıktır; admin endpoint’leri için ileride token/role tabanlı kısıtlama eklenebilir.

---

## 📝 Lisans ve Geliştirici

- **Proje adı:** Nikutek  
- **Açıklama:** Nikutek Website  
- **GroupId:** com.example  
- **ArtifactId:** nikutek  

Detaylı lisans ve geliştirici bilgisi `pom.xml` içinde güncellenebilir.
