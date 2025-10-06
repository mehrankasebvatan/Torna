# Torna - ISO8583 Message Builder & Parser

این برنامه به سفارش شرکت **ترنا سیستم** برای مصاحبه اندروید آماده شده است.

**توسعه‌دهنده:** مهران کاسب وطن

## دانلود: https://github.com/mehrankasebvatan/Torna/raw/refs/heads/master/app/app-debug.apk

## نحوه اجرا

### پیش‌نیازها

- Android Studio Arctic Fox یا بالاتر
- Android SDK API Level 24 یا بالاتر
- Kotlin 1.8 یا بالاتر

### مراحل نصب و اجرا

1. **دانلود پروژه:**
   ```bash
   git clone https://github.com/mehrankasebvatan/Torna.git
   cd Torna
   ```

2. **باز کردن در Android Studio:**
    - Android Studio را باز کنید
    - گزینه "Open an existing project" را انتخاب کنید
    - پوشه پروژه را انتخاب کنید

3. **همگام‌سازی و اجرا:**
    - روی "Sync Project with Gradle Files" کلیک کنید
    - دستگاه یا شبیه‌ساز اندروید را متصل کنید
    - روی دکمه "Run" کلیک کنید

## توضیح کوتاه کد

### ساختار پروژه

```
app/src/main/java/ir/kasebvatan/torna/
├── data/                    # مدل‌های داده
│   ├── ContactModel.kt      # مدل اطلاعات تماس
│   ├── FieldModel.kt        # مدل فیلدهای ISO8583
│   └── Cache.kt             # مدیریت کش STAN
├── presentation/
│   ├── screens/
│   │   ├── builder/         # صفحه ساخت پیام ISO8583
│   │   ├── parser/          # صفحه تجزیه پیام ISO8583
│   │   ├── contact/         # صفحه اطلاعات تماس
│   │   └── home/            # صفحه اصلی
│   ├── component/           # کامپوننت‌های قابل استفاده مجدد
│   ├── navigation/          # مدیریت ناوبری
│   └── theme/               # تم و رنگ‌بندی
└── domain/                  # منطق کسب‌وکار
```

### معماری برنامه

- **MVVM Pattern**: استفاده از ViewModel برای مدیریت state
- **Jetpack Compose**: UI مدرن و declarative
- **Material Design 3**: طراحی مطابق با استانداردهای گوگل

## نمونه ورودی/خروجی

### Builder Screen (ساخت پیام)

**ورودی:**

- **PAN (Primary Account Number):** `6274123456789012`
- **Amount:** `5000`

**خروجی:**

```
ISO8583 Message: 020052200000000000001662741234567890120000000050000714133015000056
```

**فیلدهای تولید شده:**

- Field 2: Primary Account Number (6274123456789012)
- Field 4: Transaction Amount (000000005000)
- Field 7: Transmission Date Time (0714133015)
- Field 11: System Trace Audit Number (000056)

### Parser Screen (تجزیه پیام)

**ورودی:**

```
020052200000000000001662741234567890120000000050000714133015000056
```

**خروجی:**

```
Field 2: 6274123456789012 (Primary Account Number)
Field 4: 000000005000 (Transaction Amount)
Field 7: 0714133015 (Transmission Date Time)
Field 11: 000056 (System Trace Audit Number)
```

## فرمت پیام کامل

### ساختار پیام ISO8583

```
[MTI][Primary Bitmap][Data Elements]
```

### توضیح اجزاء:

1. **MTI (Message Type Indicator):** `0200`
    - نوع پیام: درخواست مالی

2. **Primary Bitmap:** `5220000000000000`
    - نشان‌دهنده فیلدهای موجود در پیام
    - بیت‌های فعال: 2, 4, 7, 11

3. **Data Elements:**
    - **Field 2:** شماره کارت (PAN)
    - **Field 4:** مبلغ تراکنش
    - **Field 7:** تاریخ و زمان انتقال
    - **Field 11:** شماره ردیابی سیستم (STAN)

### نمونه کامل:

```
0200 5220000000000000 166274123456789012 000000005000 0714133015 000056
│    │                 │                  │            │          │
│    │                 │                  │            │          └─ STAN
│    │                 │                  │            └─ Date/Time
│    │                 │                  └─ Amount
│    │                 │               
│    │                 └─ PAN
│    └─ Primary Bitmap
└─ MTI
```

## افزایشی STAN

### منطق STAN (System Trace Audit Number)

**STAN** یک شماره منحصر به فرد 6 رقمی است که برای ردیابی تراکنش‌ها استفاده می‌شود.

#### ویژگی‌های STAN:

- **محدوده:** 000001 تا 999999
- **افزایشی:** هر بار +1
- **بازنشانی:** پس از رسیدن به 999999، به 000001 برمی‌گردد
- **ماندگار:** در SharedPreferences ذخیره می‌شود

#### پیاده‌سازی:

```kotlin
fun getStan(): String {
   if (Cache["stan", 0] == 999999) Cache["stan"] = 1
   else Cache["stan"] = Cache["stan", 0] + 1
   val stanCache = Cache["stan", 0].toString()
   return if (stanCache.length == 6) stanCache
   else "0".repeat(6 - stanCache.length) + stanCache
}
```

#### مثال تغییرات STAN:

```
تراکنش 1: 000001
تراکنش 2: 000002
تراکنش 3: 000003
...
تراکنش 999999: 999999
تراکنش 1000000: 000001 (بازنشانی)
```

## توضیح منطق

### Builder Screen Logic:

1. **ولیدیشن ورودی:**
    - PAN باید دقیقاً 16 رقم باشد
    - Amount نباید خالی باشد

2. **ساخت پیام:**
    - MTI: "0200" (درخواست مالی)
    - Primary Bitmap محاسبه می‌شود
    - STAN به صورت خودکار افزایش می‌یابد
    - تاریخ/زمان فعلی تولید می‌شود

3. **نمایش نتیجه:**
    - پیام کامل ISO8583
    - جزئیات هر فیلد
    - قابلیت کپی کردن

### Parser Screen Logic:

1. **تجزیه پیام:**
    - استخراج MTI
    - تجزیه Primary Bitmap
    - شناسایی فیلدهای موجود

2. **نمایش اطلاعات:**
    - لیست فیلدهای شناسایی شده
    - مقدار و نوع هر فیلد
    - نام توضیحی فیلد

### Contact Screen Logic:

- استفاده از LazyVerticalGrid برای نمایش 2 ستونی
- Intent برای ارسال ایمیل و تماس تلفنی
- باز کردن لینک ها در مرورگر

### Architecture Benefits:

- **Separation of Concerns:** جداسازی منطق از UI
- **Testability:** قابلیت تست آسان
- **Maintainability:** نگهداری و توسعه آسان
- **Reusability:** قابلیت استفاده مجدد کامپوننت‌ها

---

## Contact Information

- **توسعه‌دهنده:** مهران کاسب وطن
- **ایمیل:** mehran.kasebvatan@gmail.com
- **تلفن:** 09216380039
- **GitHub:** https://github.com/mehrankasebvatan/Torna.git