package com.example.badmintoncourtmanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.badmintoncourtmanager.model.*;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "BadmintonCourtManager.db";
    private static final int DATABASE_VERSION = 4; // Increased to 4 to fix crash

    // Table names
    private static final String TABLE_USERS = "users";
    private static final String TABLE_FIELDS = "fields";
    private static final String TABLE_SERVICES = "services";
    private static final String TABLE_BOOKINGS = "bookings";
    private static final String TABLE_SERVICE_USAGE = "service_usage";
    private static final String TABLE_EXPENSES = "expenses";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create users table
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "username TEXT UNIQUE NOT NULL,"
                + "password TEXT NOT NULL,"
                + "full_name TEXT,"
                + "email TEXT,"
                + "phone TEXT"
                + ")";
        db.execSQL(CREATE_USERS_TABLE);

        // Create fields table
        String CREATE_FIELDS_TABLE = "CREATE TABLE " + TABLE_FIELDS + "("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "name TEXT NOT NULL,"
                + "type TEXT,"
                + "price_per_hour REAL,"
                + "status TEXT,"
                + "description TEXT"
                + ")";
        db.execSQL(CREATE_FIELDS_TABLE);

        // Create services table
        String CREATE_SERVICES_TABLE = "CREATE TABLE " + TABLE_SERVICES + "("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "name TEXT NOT NULL,"
                + "price REAL,"
                + "unit TEXT,"
                + "quantity INTEGER,"
                + "description TEXT"
                + ")";
        db.execSQL(CREATE_SERVICES_TABLE);

        // Create bookings table
        String CREATE_BOOKINGS_TABLE = "CREATE TABLE " + TABLE_BOOKINGS + "("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "field_id INTEGER,"
                + "customer_name TEXT,"
                + "customer_phone TEXT,"
                + "date TEXT,"
                + "start_time TEXT,"
                + "end_time TEXT,"
                + "total_price REAL,"
                + "status TEXT,"
                + "notes TEXT,"
                + "FOREIGN KEY(field_id) REFERENCES " + TABLE_FIELDS + "(id)"
                + ")";
        db.execSQL(CREATE_BOOKINGS_TABLE);

        // Create service_usage table
        String CREATE_SERVICE_USAGE_TABLE = "CREATE TABLE " + TABLE_SERVICE_USAGE + "("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "booking_id INTEGER,"
                + "service_id INTEGER,"
                + "quantity INTEGER,"
                + "price REAL,"
                + "total_price REAL,"
                + "FOREIGN KEY(booking_id) REFERENCES " + TABLE_BOOKINGS + "(id),"
                + "FOREIGN KEY(service_id) REFERENCES " + TABLE_SERVICES + "(id)"
                + ")";
        db.execSQL(CREATE_SERVICE_USAGE_TABLE);

        // Create expenses table
        String CREATE_EXPENSES_TABLE = "CREATE TABLE " + TABLE_EXPENSES + "("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "category TEXT,"
                + "amount REAL,"
                + "date TEXT,"
                + "description TEXT"
                + ")";
        db.execSQL(CREATE_EXPENSES_TABLE);

        // Insert default admin user
        ContentValues values = new ContentValues();
        values.put("username", "admin");
        values.put("password", "admin123");
        values.put("full_name", "Quản trị viên");
        values.put("email", "admin@badminton.com");
        values.put("phone", "0123456789");
        db.insert(TABLE_USERS, null, values);

        // Insert sample fields
        insertSampleField(db, "Sân 1", "Đơn", 50000, "Hoạt động", "Sân cầu lông đơn tiêu chuẩn");
        insertSampleField(db, "Sân 2", "Đôi", 80000, "Hoạt động", "Sân cầu lông đôi rộng rãi");
        insertSampleField(db, "Sân 3 VIP", "VIP", 120000, "Hoạt động", "Sân VIP có điều hòa");
        insertSampleField(db, "Sân 4", "Đơn", 50000, "Bảo trì", "Đang bảo trì");

        // Insert sample services
        insertSampleService(db, "Cho thuê vợt", 20000, "Cái", 10, "Vợt cầu lông chất lượng");
        insertSampleService(db, "Cho thuê giày", 15000, "Đôi", 8, "Giày thể thao chuyên dụng");
        insertSampleService(db, "Nước suối", 5000, "Chai", 50, "Nước suối 500ml");
        insertSampleService(db, "Nước tăng lực", 15000, "Chai", 30, "Nước tăng lực Revive");

        // Seed Bookings
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault());
        String today = sdf.format(new java.util.Date());

        // 1. Paid bookings (Revenue)
        db.execSQL("INSERT INTO " + TABLE_BOOKINGS
                + " (field_id, customer_name, customer_phone, date, start_time, end_time, total_price, status) VALUES "
                + "(1, 'Nguyễn Văn An', '0909123456', '" + today + "', '08:00', '10:00', 150000, 'Đã thanh toán')");
        db.execSQL("INSERT INTO " + TABLE_BOOKINGS
                + " (field_id, customer_name, customer_phone, date, start_time, end_time, total_price, status) VALUES "
                + "(2, 'Trần Thị Bình', '0909123457', '" + today + "', '09:00', '11:00', 160000, 'Đã thanh toán')");

        // 2. Pending bookings (Potential Revenue)
        db.execSQL("INSERT INTO " + TABLE_BOOKINGS
                + " (field_id, customer_name, customer_phone, date, start_time, end_time, total_price, status) VALUES "
                + "(1, 'Lê Văn Cường', '0909123458', '" + today + "', '17:00', '19:00', 150000, 'Đã đặt')");
    }

    private void insertSampleField(SQLiteDatabase db, String name, String type, double price, String status,
            String desc) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("type", type);
        values.put("price_per_hour", price);
        values.put("status", status);
        values.put("description", desc);
        db.insert(TABLE_FIELDS, null, values);
    }

    private void insertSampleService(SQLiteDatabase db, String name, double price, String unit, int qty, String desc) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("price", price);
        values.put("unit", unit);
        values.put("quantity", qty);
        values.put("description", desc);
        db.insert(TABLE_SERVICES, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERVICE_USAGE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKINGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERVICES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FIELDS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // ==================== USER METHODS ====================
    public long addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", user.getUsername());
        values.put("password", user.getPassword());
        values.put("full_name", user.getFullName());
        values.put("email", user.getEmail());
        values.put("phone", user.getPhone());
        long id = db.insert(TABLE_USERS, null, values);
        db.close();
        return id;
    }

    public User checkLogin(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                null,
                "username=? AND password=?",
                new String[] { username, password },
                null, null, null);

        User user = null;
        if (cursor != null && cursor.moveToFirst()) {
            user = new User(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5));
            cursor.close();
        }
        db.close();
        return user;
    }

    public boolean isUsernameExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[] { "id" },
                "username=?",
                new String[] { username },
                null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    // ==================== FIELD METHODS ====================
    public long addField(Field field) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", field.getName());
        values.put("type", field.getType());
        values.put("price_per_hour", field.getPricePerHour());
        values.put("status", field.getStatus());
        values.put("description", field.getDescription());
        long id = db.insert(TABLE_FIELDS, null, values);
        db.close();
        return id;
    }

    public int updateField(Field field) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", field.getName());
        values.put("type", field.getType());
        values.put("price_per_hour", field.getPricePerHour());
        values.put("status", field.getStatus());
        values.put("description", field.getDescription());
        int result = db.update(TABLE_FIELDS, values, "id=?", new String[] { String.valueOf(field.getId()) });
        db.close();
        return result;
    }

    public boolean updateFieldStatus(int id, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("status", status);
        int result = db.update(TABLE_FIELDS, values, "id=?", new String[] { String.valueOf(id) });
        db.close();
        return result > 0;
    }

    public void deleteField(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FIELDS, "id=?", new String[] { String.valueOf(id) });
        db.close();
    }

    public List<Field> getAllFields() {
        List<Field> fields = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_FIELDS, null);

        if (cursor.moveToFirst()) {
            do {
                Field field = new Field(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getDouble(3),
                        cursor.getString(4),
                        cursor.getString(5));
                fields.add(field);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return fields;
    }

    public List<Field> getAvailableFields() {
        List<Field> fields = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_FIELDS,
                null,
                "status=?",
                new String[] { "Hoạt động" },
                null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Field field = new Field(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getDouble(3),
                        cursor.getString(4),
                        cursor.getString(5));
                fields.add(field);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return fields;
    }

    public Field getFieldById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_FIELDS,
                null,
                "id=?",
                new String[] { String.valueOf(id) },
                null, null, null);

        Field field = null;
        if (cursor != null && cursor.moveToFirst()) {
            field = new Field(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getDouble(3),
                    cursor.getString(4),
                    cursor.getString(5));
            cursor.close();
        }
        db.close();
        return field;
    }

    // ==================== SERVICE METHODS ====================
    public long addService(Service service) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", service.getName());
        values.put("price", service.getPrice());
        values.put("unit", service.getUnit());
        values.put("quantity", service.getQuantity());
        values.put("description", service.getDescription());
        long id = db.insert(TABLE_SERVICES, null, values);
        db.close();
        return id;
    }

    public int updateService(Service service) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", service.getName());
        values.put("price", service.getPrice());
        values.put("unit", service.getUnit());
        values.put("quantity", service.getQuantity());
        values.put("description", service.getDescription());
        int result = db.update(TABLE_SERVICES, values, "id=?", new String[] { String.valueOf(service.getId()) });
        db.close();
        return result;
    }

    public void deleteService(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SERVICES, "id=?", new String[] { String.valueOf(id) });
        db.close();
    }

    public List<Service> getAllServices() {
        List<Service> services = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SERVICES, null);

        if (cursor.moveToFirst()) {
            do {
                Service service = new Service(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getDouble(2),
                        cursor.getString(3),
                        cursor.getInt(4),
                        cursor.getString(5));
                services.add(service);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return services;
    }

    public Service getServiceById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SERVICES,
                null,
                "id=?",
                new String[] { String.valueOf(id) },
                null, null, null);

        Service service = null;
        if (cursor != null && cursor.moveToFirst()) {
            service = new Service(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getDouble(2),
                    cursor.getString(3),
                    cursor.getInt(4),
                    cursor.getString(5));
            cursor.close();
        }
        db.close();
        return service;
    }

    // ==================== BOOKING METHODS ====================
    public long addBooking(Booking booking) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("field_id", booking.getFieldId());
        values.put("customer_name", booking.getCustomerName());
        values.put("customer_phone", booking.getCustomerPhone());
        values.put("date", booking.getDate());
        values.put("start_time", booking.getStartTime());
        values.put("end_time", booking.getEndTime());
        values.put("total_price", booking.getTotalPrice());
        values.put("status", booking.getStatus() != null ? booking.getStatus() : "Đã đặt");
        values.put("notes", booking.getNotes());
        long id = db.insert(TABLE_BOOKINGS, null, values);
        db.close();
        return id;
    }

    public int updateBooking(Booking booking) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("field_id", booking.getFieldId());
        values.put("customer_name", booking.getCustomerName());
        values.put("customer_phone", booking.getCustomerPhone());
        values.put("date", booking.getDate());
        values.put("start_time", booking.getStartTime());
        values.put("end_time", booking.getEndTime());
        values.put("total_price", booking.getTotalPrice());
        values.put("status", booking.getStatus() != null ? booking.getStatus() : "Đã đặt");
        values.put("notes", booking.getNotes());
        int result = db.update(TABLE_BOOKINGS, values, "id=?", new String[] { String.valueOf(booking.getId()) });
        db.close();
        return result;
    }

    public List<Booking> getAllBookings() {
        List<Booking> bookings = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        // Use IFNULL to handle deleted fields
        String query = "SELECT b.*, IFNULL(f.name, 'Sân không xác định') FROM " + TABLE_BOOKINGS + " b " +
                "LEFT JOIN " + TABLE_FIELDS + " f ON b.field_id = f.id " +
                "ORDER BY b.date DESC, b.start_time DESC";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Booking booking = new Booking(
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getString(10), // IFNULL used
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getDouble(7),
                        cursor.getString(8),
                        cursor.getString(9));
                bookings.add(booking);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return bookings;
    }

    public Booking getBookingById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        // Use IFNULL to handle deleted fields
        String query = "SELECT b.*, IFNULL(f.name, 'Sân không xác định') FROM " + TABLE_BOOKINGS + " b " +
                "LEFT JOIN " + TABLE_FIELDS + " f ON b.field_id = f.id " +
                "WHERE b.id = ?";
        Cursor cursor = db.rawQuery(query, new String[] { String.valueOf(id) });

        Booking booking = null;
        if (cursor != null && cursor.moveToFirst()) {
            booking = new Booking(
                    cursor.getInt(0),
                    cursor.getInt(1),
                    cursor.getString(10), // IFNULL used
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getDouble(7),
                    cursor.getString(8),
                    cursor.getString(9));
            cursor.close();
        }
        db.close();
        return booking;
    }

    // Overloaded method to add booking with services
    public long addBooking(Booking booking, List<ServiceUsage> serviceUsages) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put("field_id", booking.getFieldId());
            values.put("customer_name", booking.getCustomerName());
            values.put("customer_phone", booking.getCustomerPhone());
            values.put("date", booking.getDate());
            values.put("start_time", booking.getStartTime());
            values.put("end_time", booking.getEndTime());
            values.put("total_price", booking.getTotalPrice());
            values.put("status", booking.getStatus() != null ? booking.getStatus() : "Đã đặt");
            values.put("notes", booking.getNotes());
            long bookingId = db.insert(TABLE_BOOKINGS, null, values);

            if (bookingId > 0 && serviceUsages != null) {
                for (ServiceUsage usage : serviceUsages) {
                    ContentValues serviceValues = new ContentValues();
                    serviceValues.put("booking_id", bookingId);
                    serviceValues.put("service_id", usage.getServiceId());
                    serviceValues.put("quantity", usage.getQuantity());
                    serviceValues.put("price", usage.getPrice());
                    serviceValues.put("total_price", usage.getPrice() * usage.getQuantity());
                    db.insert(TABLE_SERVICE_USAGE, null, serviceValues);
                }
            }

            db.setTransactionSuccessful();
            return bookingId;
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    // Overloaded method to update booking with services
    public int updateBooking(Booking booking, List<ServiceUsage> serviceUsages) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put("field_id", booking.getFieldId());
            values.put("customer_name", booking.getCustomerName());
            values.put("customer_phone", booking.getCustomerPhone());
            values.put("date", booking.getDate());
            values.put("start_time", booking.getStartTime());
            values.put("end_time", booking.getEndTime());
            values.put("total_price", booking.getTotalPrice());
            values.put("status", booking.getStatus() != null ? booking.getStatus() : "Đã đặt");
            values.put("notes", booking.getNotes());
            int result = db.update(TABLE_BOOKINGS, values, "id=?", new String[] { String.valueOf(booking.getId()) });

            if (result > 0) {
                // Delete old service usages
                db.delete(TABLE_SERVICE_USAGE, "booking_id=?", new String[] { String.valueOf(booking.getId()) });

                // Insert new service usages
                if (serviceUsages != null) {
                    for (ServiceUsage usage : serviceUsages) {
                        ContentValues serviceValues = new ContentValues();
                        serviceValues.put("booking_id", booking.getId());
                        serviceValues.put("service_id", usage.getServiceId());
                        serviceValues.put("quantity", usage.getQuantity());
                        serviceValues.put("price", usage.getPrice());
                        serviceValues.put("total_price", usage.getPrice() * usage.getQuantity());
                        db.insert(TABLE_SERVICE_USAGE, null, serviceValues);
                    }
                }
            }

            db.setTransactionSuccessful();
            return result;
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    // Update booking status
    public boolean updateBookingStatus(int bookingId, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("status", status);
        int result = db.update(TABLE_BOOKINGS, values, "id=?", new String[] { String.valueOf(bookingId) });
        db.close();
        return result > 0;
    }

    // Delete booking (returns boolean)
    public boolean deleteBooking(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            // Delete related service usage first
            db.delete(TABLE_SERVICE_USAGE, "booking_id=?", new String[] { String.valueOf(id) });
            int result = db.delete(TABLE_BOOKINGS, "id=?", new String[] { String.valueOf(id) });
            db.setTransactionSuccessful();
            return result > 0;
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public boolean isBookingOverlap(int bookingId, int fieldId, String date, String startTime, String endTime) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT id FROM " + TABLE_BOOKINGS + " WHERE " +
                "field_id = ? AND date = ? AND id != ? AND " +
                "(" +
                "(start_time < ? AND end_time > ?) OR " + // New start is inside existing
                "(start_time < ? AND end_time > ?) OR " + // New end is inside existing
                "(start_time >= ? AND end_time <= ?)" + // Existing is inside new (Covered by above, but ensuring full
                                                        // coverage)
                ")";

        // Simplified Logic: Overlap if (StartA < EndB) and (EndA > StartB)
        // Existing: Start, End
        // New: newStart, newEnd
        // Query: SELECT * WHERE field = ? AND date = ? AND id != ? AND (start_time < ?
        // AND end_time > ?)

        String optimizedQuery = "SELECT id FROM " + TABLE_BOOKINGS + " WHERE " +
                "field_id = ? AND date = ? AND id != ? AND " +
                "(start_time < ? AND end_time > ?)";

        Cursor cursor = db.rawQuery(optimizedQuery, new String[] {
                String.valueOf(fieldId),
                date,
                String.valueOf(bookingId),
                endTime,
                startTime
        });

        boolean overlap = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return overlap;
    }

    // Get service usages by booking ID
    public List<ServiceUsage> getServiceUsagesByBookingId(int bookingId) {
        List<ServiceUsage> usageList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT su.*, s.name as service_name FROM " + TABLE_SERVICE_USAGE + " su " +
                        "INNER JOIN " + TABLE_SERVICES + " s ON su.service_id = s.id " +
                        "WHERE su.booking_id = ?",
                new String[] { String.valueOf(bookingId) });

        if (cursor.moveToFirst()) {
            do {
                ServiceUsage usage = new ServiceUsage();
                usage.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                usage.setBookingId(cursor.getInt(cursor.getColumnIndexOrThrow("booking_id")));
                usage.setServiceId(cursor.getInt(cursor.getColumnIndexOrThrow("service_id")));
                usage.setServiceName(cursor.getString(cursor.getColumnIndexOrThrow("service_name")));
                usage.setQuantity(cursor.getInt(cursor.getColumnIndexOrThrow("quantity")));
                usage.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow("price")));
                usageList.add(usage);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return usageList;
    }

    // ==================== SERVICE USAGE METHODS ====================
    public long addServiceUsage(ServiceUsage serviceUsage) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("booking_id", serviceUsage.getBookingId());
        values.put("service_id", serviceUsage.getServiceId());
        values.put("quantity", serviceUsage.getQuantity());
        values.put("price", serviceUsage.getPrice());
        values.put("total_price", serviceUsage.getTotalPrice());
        long id = db.insert(TABLE_SERVICE_USAGE, null, values);
        db.close();
        return id;
    }

    public List<ServiceUsage> getServiceUsageByBooking(int bookingId) {
        List<ServiceUsage> usages = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT su.*, s.name FROM " + TABLE_SERVICE_USAGE + " su " +
                "LEFT JOIN " + TABLE_SERVICES + " s ON su.service_id = s.id " +
                "WHERE su.booking_id = ?";
        Cursor cursor = db.rawQuery(query, new String[] { String.valueOf(bookingId) });

        if (cursor.moveToFirst()) {
            do {
                ServiceUsage usage = new ServiceUsage(
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getInt(2),
                        cursor.getString(6),
                        cursor.getInt(3),
                        cursor.getDouble(4),
                        cursor.getDouble(5));
                usages.add(usage);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return usages;
    }

    public void deleteServiceUsageByBooking(int bookingId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SERVICE_USAGE, "booking_id=?", new String[] { String.valueOf(bookingId) });
        db.close();
    }

    // ==================== EXPENSE METHODS ====================
    public long addExpense(Expense expense) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("category", expense.getCategory());
        values.put("amount", expense.getAmount());
        values.put("date", expense.getDate());
        values.put("description", expense.getDescription());
        long id = db.insert(TABLE_EXPENSES, null, values);
        db.close();
        return id;
    }

    public int updateExpense(Expense expense) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("category", expense.getCategory());
        values.put("amount", expense.getAmount());
        values.put("date", expense.getDate());
        values.put("description", expense.getDescription());
        int result = db.update(TABLE_EXPENSES, values, "id=?", new String[] { String.valueOf(expense.getId()) });
        db.close();
        return result;
    }

    public void deleteExpense(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EXPENSES, "id=?", new String[] { String.valueOf(id) });
        db.close();
    }

    public List<Expense> getAllExpenses() {
        List<Expense> expenses = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_EXPENSES + " ORDER BY date DESC", null);

        if (cursor.moveToFirst()) {
            do {
                Expense expense = new Expense(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getDouble(2),
                        cursor.getString(3),
                        cursor.getString(4));
                expenses.add(expense);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return expenses;
    }

    // ==================== TRANSACTION METHODS ====================
    public List<Transaction> getAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Get income from bookings
        String bookingQuery = "SELECT id, date, total_price, customer_name FROM " + TABLE_BOOKINGS +
                " WHERE status = 'Đã thanh toán' ORDER BY date DESC";
        Cursor cursor = db.rawQuery(bookingQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Transaction transaction = new Transaction(
                        cursor.getInt(0),
                        "Thu",
                        "Đặt sân",
                        cursor.getDouble(2),
                        cursor.getString(1),
                        "Khách hàng: " + cursor.getString(3));
                transactions.add(transaction);
            } while (cursor.moveToNext());
        }
        cursor.close();

        // Get expenses
        String expenseQuery = "SELECT id, date, amount, category, description FROM " + TABLE_EXPENSES +
                " ORDER BY date DESC";
        cursor = db.rawQuery(expenseQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Transaction transaction = new Transaction(
                        cursor.getInt(0),
                        "Chi",
                        cursor.getString(3),
                        cursor.getDouble(2),
                        cursor.getString(1),
                        cursor.getString(4));
                transactions.add(transaction);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return transactions;
    }

    // ==================== STATISTICS METHODS ====================
    public double getTotalRevenue() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT SUM(total_price) FROM " + TABLE_BOOKINGS + " WHERE status = 'Đã thanh toán'",
                null);
        double total = 0;
        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0);
        }
        cursor.close();
        db.close();
        return total;
    }

    public double getTotalExpenses() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(amount) FROM " + TABLE_EXPENSES, null);
        double total = 0;
        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0);
        }
        cursor.close();
        db.close();
        return total;
    }

    public int getTotalBookings() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_BOOKINGS, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return count;
    }

    public double getPendingRevenue() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT SUM(total_price) FROM " + TABLE_BOOKINGS + " WHERE status = 'Đã đặt'",
                null);
        double total = 0;
        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0);
        }
        cursor.close();
        db.close();
        return total;
    }
}
