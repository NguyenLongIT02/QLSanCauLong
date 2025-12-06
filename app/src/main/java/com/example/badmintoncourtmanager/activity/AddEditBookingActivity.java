package com.example.badmintoncourtmanager.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.badmintoncourtmanager.R;
import com.example.badmintoncourtmanager.adapter.ServiceUsageAdapter;
import com.example.badmintoncourtmanager.database.DatabaseHelper;
import com.example.badmintoncourtmanager.model.Booking;
import com.example.badmintoncourtmanager.model.Field;
import com.example.badmintoncourtmanager.model.Service;
import com.example.badmintoncourtmanager.model.ServiceUsage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddEditBookingActivity extends AppCompatActivity {
    private EditText etCustomerName, etCustomerPhone, etHours;
    private Spinner spinnerField, spinnerStartTime, spinnerEndTime;
    private TextView tvDate, tvTotalPrice;
    private ImageButton buttonBack;
    private Button btnSelectDate, btnAddService, btnSave, btnCancel;
    private RecyclerView rvServices;
    private LinearLayout layoutServices;

    private DatabaseHelper dbHelper;
    private List<Field> fieldList;
    private List<Service> serviceList;
    private List<ServiceUsage> selectedServices;
    private ServiceUsageAdapter serviceAdapter;

    private int bookingId = -1;
    private Calendar selectedDate;
    private String currentStatus = "Đã đặt";
    private java.util.Map<Integer, Integer> inventoryMap = new java.util.HashMap<>();

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_booking);

        initViews();
        dbHelper = new DatabaseHelper(this);
        selectedServices = new ArrayList<>();
        setupTimeSpinners();

        // Check if editing
        bookingId = getIntent().getIntExtra("booking_id", -1);

        loadFields();
        loadServices();
        setupRecyclerView();
        setupListeners();

        if (bookingId != -1) {
            loadBookingData();
        } else {
            selectedDate = Calendar.getInstance();
            updateDateTimeDisplay();

            // Check for pre-selected field (from AvailableFieldsActivity)
            int preFieldId = getIntent().getIntExtra("field_id", -1);
            if (preFieldId != -1) {
                for (int i = 0; i < fieldList.size(); i++) {
                    if (fieldList.get(i).getId() == preFieldId) {
                        spinnerField.setSelection(i);
                        break;
                    }
                }
            }
        }
    }

    private void initViews() {
        etCustomerName = findViewById(R.id.etCustomerName);
        etCustomerPhone = findViewById(R.id.etCustomerPhone);
        etHours = findViewById(R.id.etHours);
        spinnerField = findViewById(R.id.spinnerField);
        tvDate = findViewById(R.id.tvDate);
        spinnerStartTime = findViewById(R.id.spinnerStartTime);
        spinnerEndTime = findViewById(R.id.spinnerEndTime);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        buttonBack = findViewById(R.id.button_back);
        btnSelectDate = findViewById(R.id.btnSelectDate);
        btnAddService = findViewById(R.id.btnAddService);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
        rvServices = findViewById(R.id.rvServices);
        layoutServices = findViewById(R.id.layoutServices);
    }

    private void loadFields() {
        fieldList = dbHelper.getAvailableFields();
        if (fieldList.isEmpty()) {
            Toast.makeText(this, "Không có sân nào đang hoạt động!", Toast.LENGTH_LONG).show();
        }
        List<String> fieldNames = new ArrayList<>();
        for (Field field : fieldList) {
            fieldNames.add(field.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, fieldNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerField.setAdapter(adapter);
    }

    private void loadServices() {
        serviceList = dbHelper.getAllServices();
        inventoryMap.clear();
        for (Service s : serviceList) {
            inventoryMap.put(s.getId(), s.getQuantity());
        }
        if (serviceAdapter != null) {
            serviceAdapter.setInventoryMap(inventoryMap);
        }
    }

    private void setupTimeSpinners() {
        List<String> timeSlots = new ArrayList<>();
        int startHour = 5;
        int endHour = 23;
        for (int i = startHour; i <= endHour; i++) {
            timeSlots.add(String.format(Locale.getDefault(), "%02d:00", i));
            if (i < endHour) {
                timeSlots.add(String.format(Locale.getDefault(), "%02d:30", i));
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, timeSlots);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStartTime.setAdapter(adapter);
        spinnerEndTime.setAdapter(adapter);

        // Default selection: 7:00 - 8:00
        int startIndex = timeSlots.indexOf("07:00");
        int endIndex = timeSlots.indexOf("08:00");
        if (startIndex >= 0)
            spinnerStartTime.setSelection(startIndex);
        if (endIndex >= 0)
            spinnerEndTime.setSelection(endIndex);
    }

    private void setupRecyclerView() {
        serviceAdapter = new ServiceUsageAdapter(this, selectedServices,
                new ServiceUsageAdapter.OnServiceUsageListener() {
                    @Override
                    public void onDelete(int position) {
                        selectedServices.remove(position);
                        serviceAdapter.notifyItemRemoved(position);
                        calculateTotalPrice();
                        updateServicesVisibility();
                    }

                    @Override
                    public void onQuantityChanged(int position, int newQuantity) {
                        // The adapter updates the object, we just need to recalculate total
                        calculateTotalPrice();
                    }
                });
        rvServices.setLayoutManager(new LinearLayoutManager(this));
        serviceAdapter.setInventoryMap(inventoryMap);
        rvServices.setAdapter(serviceAdapter);
    }

    private void setupListeners() {
        btnSelectDate.setOnClickListener(v -> showDatePicker());
        buttonBack.setOnClickListener(v -> finish());
        btnAddService.setOnClickListener(v -> showServiceDialog());
        btnSave.setOnClickListener(v -> saveBooking());
        btnCancel.setOnClickListener(v -> finish());

        spinnerField.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                calculateTotalPrice();
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
            }
        });

        spinnerStartTime.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                calculateTotalPrice();
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
            }
        });

        spinnerEndTime.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                calculateTotalPrice();
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
            }
        });
    }

    private void showDatePicker() {
        DatePickerDialog dialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    selectedDate.set(year, month, dayOfMonth);
                    updateDateTimeDisplay();
                },
                selectedDate.get(Calendar.YEAR),
                selectedDate.get(Calendar.MONTH),
                selectedDate.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private void showServiceDialog() {
        if (serviceList.isEmpty()) {
            Toast.makeText(this, "Không có dịch vụ nào", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] serviceNames = new String[serviceList.size()];
        for (int i = 0; i < serviceList.size(); i++) {
            serviceNames[i] = serviceList.get(i).getName() + " - " +
                    String.format("%,.0f đ", serviceList.get(i).getPrice());
        }

        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Chọn dịch vụ")
                .setItems(serviceNames, (dialog, which) -> {
                    Service service = serviceList.get(which);
                    addServiceUsage(service);
                })
                .show();
    }

    private void addServiceUsage(Service service) {
        int maxQty = inventoryMap.getOrDefault(service.getId(), 0);

        for (ServiceUsage usage : selectedServices) {
            if (usage.getServiceId() == service.getId()) {
                if (usage.getQuantity() < maxQty) {
                    usage.setQuantity(usage.getQuantity() + 1);
                    serviceAdapter.notifyDataSetChanged();
                    calculateTotalPrice();
                } else {
                    Toast.makeText(this, "Đã đạt giới hạn tồn kho: " + maxQty, Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }

        if (maxQty > 0) {
            ServiceUsage usage = new ServiceUsage();
            usage.setServiceId(service.getId());
            usage.setServiceName(service.getName());
            usage.setPrice(service.getPrice());
            usage.setQuantity(1);

            selectedServices.add(usage);
            serviceAdapter.notifyItemInserted(selectedServices.size() - 1);
            calculateTotalPrice();
            updateServicesVisibility();
        } else {
            Toast.makeText(this, "Dịch vụ này đã hết hàng!", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateServicesVisibility() {
        layoutServices.setVisibility(selectedServices.isEmpty() ? View.GONE : View.VISIBLE);
    }

    private void updateDateTimeDisplay() {
        tvDate.setText(dateFormat.format(selectedDate.getTime()));
    }

    private double calculateHours() {
        try {
            String startStr = spinnerStartTime.getSelectedItem().toString();
            String endStr = spinnerEndTime.getSelectedItem().toString();

            java.util.Date startDate = timeFormat.parse(startStr);
            java.util.Date endDate = timeFormat.parse(endStr);

            if (startDate != null && endDate != null) {
                long diff = endDate.getTime() - startDate.getTime();
                return diff / (1000.0 * 60 * 60);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private void calculateTotalPrice() {
        if (fieldList == null || fieldList.isEmpty() || spinnerField.getSelectedItemPosition() < 0) {
            return;
        }

        Field selectedField = fieldList.get(spinnerField.getSelectedItemPosition());
        double hours = calculateHours();

        // Prevent negative total if end time < start time
        if (hours < 0)
            hours = 0;

        double fieldPrice = selectedField.getPricePerHour() * hours;

        double servicePrice = 0;
        for (ServiceUsage usage : selectedServices) {
            servicePrice += usage.getPrice() * usage.getQuantity();
        }

        double total = fieldPrice + servicePrice;
        tvTotalPrice.setText(String.format("Tổng tiền: %,.0f đ", total));
        etHours.setText(String.format("%.1f", hours));
    }

    private void loadBookingData() {
        Booking booking = dbHelper.getBookingById(bookingId);
        if (booking != null) {
            etCustomerName.setText(booking.getCustomerName());
            etCustomerPhone.setText(booking.getCustomerPhone());
            currentStatus = booking.getStatus(); // Save current status

            // Set field
            for (int i = 0; i < fieldList.size(); i++) {
                if (fieldList.get(i).getId() == booking.getFieldId()) {
                    spinnerField.setSelection(i);
                    break;
                }
            }

            // Set date
            try {
                selectedDate.setTime(dateFormat.parse(booking.getDate()));
                updateDateTimeDisplay();

                // Set time spinners
                @SuppressWarnings("unchecked")
                ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinnerStartTime.getAdapter();
                int startPos = adapter.getPosition(booking.getStartTime());
                int endPos = adapter.getPosition(booking.getEndTime());

                if (startPos >= 0)
                    spinnerStartTime.setSelection(startPos);
                if (endPos >= 0)
                    spinnerEndTime.setSelection(endPos);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Load services
            selectedServices.clear();
            selectedServices.addAll(dbHelper.getServiceUsagesByBookingId(bookingId));
            serviceAdapter.notifyDataSetChanged();
            updateServicesVisibility();

            calculateTotalPrice();
        }
    }

    private void saveBooking() {
        String customerName = etCustomerName.getText().toString().trim();
        String customerPhone = etCustomerPhone.getText().toString().trim();

        if (customerName.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập tên khách hàng", Toast.LENGTH_SHORT).show();
            return;
        }

        if (customerPhone.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập số điện thoại", Toast.LENGTH_SHORT).show();
            return;
        }

        if (fieldList.isEmpty() || spinnerField.getSelectedItemPosition() < 0) {
            Toast.makeText(this, "Vui lòng chọn sân", Toast.LENGTH_SHORT).show();
            return;
        }

        double hours = calculateHours();
        if (hours <= 0) {
            Toast.makeText(this, "Thời gian kết thúc phải lớn hơn thời gian bắt đầu", Toast.LENGTH_SHORT).show();
            return;
        }

        Field selectedField = fieldList.get(spinnerField.getSelectedItemPosition());
        double fieldPrice = selectedField.getPricePerHour() * hours;
        double servicePrice = 0;
        for (ServiceUsage usage : selectedServices) {
            servicePrice += usage.getPrice() * usage.getQuantity();
        }
        double totalPrice = fieldPrice + servicePrice;

        Booking booking = new Booking();
        if (bookingId != -1) {
            booking.setId(bookingId);
        }
        booking.setFieldId(selectedField.getId());
        booking.setFieldName(selectedField.getName());
        booking.setCustomerName(customerName);
        booking.setCustomerPhone(customerPhone);
        booking.setDate(dateFormat.format(selectedDate.getTime()));
        booking.setStartTime(spinnerStartTime.getSelectedItem().toString());
        booking.setEndTime(spinnerEndTime.getSelectedItem().toString());
        booking.setHours(hours);
        booking.setTotalPrice(totalPrice);
        booking.setStatus(currentStatus); // Use preserved status

        long result;
        if (bookingId == -1) {
            result = dbHelper.addBooking(booking, selectedServices);
        } else {
            result = dbHelper.updateBooking(booking, selectedServices);
        }

        if (result > 0) {
            Toast.makeText(this, bookingId == -1 ? "Thêm đặt sân thành công" : "Cập nhật đặt sân thành công",
                    Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
        }
    }
}
