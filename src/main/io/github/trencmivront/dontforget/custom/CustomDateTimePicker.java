package main.io.github.trencmivront.dontforget.custom;

import java.awt.Color;
import java.awt.Font;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.components.DateTimePicker;

public class CustomDateTimePicker extends DateTimePicker {

	private static final long serialVersionUID = 1L;

	// ── Dark palette ──────────────────────────────────────────────────────────
	/** Deep charcoal — main calendar background */
	private static final Color BG_PANEL        = new Color(0x1E, 0x20, 0x2B); // #1E202B
	/** Slightly lighter surface — navigation bar */
	private static final Color BG_NAV          = new Color(0x25, 0x28, 0x36); // #25283 6
	/** Day-cell background */
	private static final Color BG_DATES        = new Color(0x1E, 0x20, 0x2B); // #1E202B
	/** Accent purple-blue — selected date */
	private static final Color ACCENT          = new Color(0x5E, 0x6A, 0xD2); // #5E6AD2
	/** Softer accent for today's date */
	private static final Color ACCENT_TODAY    = new Color(0x3D, 0x9E, 0xC8); // #3D9EC8
	/** Highlighted / hover cell */
	private static final Color BG_HIGHLIGHT    = new Color(0x2E, 0x31, 0x44); // #2E3144
	/** Primary text — bright white */
	private static final Color TEXT_PRIMARY    = new Color(0xE8, 0xEA, 0xF0); // #E8EAF0
	/** Secondary text — muted grey for nav labels */
	private static final Color TEXT_SECONDARY  = new Color(0xA0, 0xA8, 0xC0); // #A0A8C0
	/** Disabled / greyed-out dates (other months) */
	private static final Color TEXT_DISABLED   = new Color(0x4A, 0x4E, 0x6A); // #4A4E6A
	/** Text on accent (selected date) — almost white */
	private static final Color TEXT_ON_ACCENT  = new Color(0xF5, 0xF6, 0xFF); // #F5F6FF

	public CustomDateTimePicker() {
		
		// ── Date picker settings ───────────────────────────────────────────────
		DatePickerSettings datePickerSettings = new DatePickerSettings();

		setColors(datePickerSettings);
		
		// ── Apply settings ─────────────────────────────────────────────────────
		getDatePicker().setSettings(datePickerSettings);
		datePickerSettings.setDateRangeLimits(LocalDate.now(), LocalDate.MAX);
	}
	
	private void setColors(DatePickerSettings datePickerSettings) {
		// --- Overall calendar panel ---
		datePickerSettings.setColor(DatePickerSettings.DateArea.BackgroundOverallCalendarPanel,    BG_PANEL);
		datePickerSettings.setColor(DatePickerSettings.DateArea.BackgroundTodayLabel,              BG_PANEL);
		datePickerSettings.setColor(DatePickerSettings.DateArea.BackgroundClearLabel,              BG_PANEL);

		// --- Navigation bar (month / year header) ---
		datePickerSettings.setColor(DatePickerSettings.DateArea.BackgroundMonthAndYearMenuLabels,  BG_NAV);
		datePickerSettings.setColor(DatePickerSettings.DateArea.BackgroundMonthAndYearNavigationButtons, BG_NAV);
		datePickerSettings.setColor(DatePickerSettings.DateArea.TextMonthAndYearMenuLabels,        TEXT_PRIMARY);
		datePickerSettings.setColor(DatePickerSettings.DateArea.TextMonthAndYearNavigationButtons, TEXT_PRIMARY);

		// --- Day-of-week header row ---
		datePickerSettings.setColor(DatePickerSettings.DateArea.BackgroundCalendarPanelLabelsOnHover, BG_HIGHLIGHT);
		datePickerSettings.setColor(DatePickerSettings.DateArea.TextCalendarPanelLabelsOnHover,         TEXT_SECONDARY);
		datePickerSettings.setColor(DatePickerSettings.DateArea.CalendarTextWeekNumbers,      TEXT_DISABLED);

		// --- Normal date cells ---
		datePickerSettings.setColor(DatePickerSettings.DateArea.CalendarBackgroundNormalDates,     BG_DATES);
		datePickerSettings.setColor(DatePickerSettings.DateArea.CalendarTextNormalDates,           TEXT_PRIMARY);
		datePickerSettings.setColor(DatePickerSettings.DateArea.CalendarBackgroundNormalDates,     BG_DATES);

		// --- Highlighted (hover) cells ---
		datePickerSettings.setColor(DatePickerSettings.DateArea.CalendarDefaultBackgroundHighlightedDates, BG_HIGHLIGHT);
		datePickerSettings.setColor(DatePickerSettings.DateArea.CalendarDefaultTextHighlightedDates,      TEXT_PRIMARY);

		// --- Dates from previous / next months ("other months") ---
		datePickerSettings.setColor(DatePickerSettings.DateArea.DatePickerTextInvalidDate,          TEXT_DISABLED);

		// --- Veto / invalid dates ---
		datePickerSettings.setColor(DatePickerSettings.DateArea.CalendarBorderSelectedDate,        ACCENT);

		// --- Selected date ---
		datePickerSettings.setColor(DatePickerSettings.DateArea.CalendarBackgroundSelectedDate,    ACCENT);
		datePickerSettings.setColor(DatePickerSettings.DateArea.CalendarBackgroundSelectedDate,          TEXT_ON_ACCENT);

		// --- Today label ---
		datePickerSettings.setColor(DatePickerSettings.DateArea.BackgroundTodayLabel,           ACCENT_TODAY);
		datePickerSettings.setColor(DatePickerSettings.DateArea.TextTodayLabel,                   ACCENT_TODAY);
		datePickerSettings.setColor(DatePickerSettings.DateArea.TextClearLabel,                   TEXT_SECONDARY);

		// --- Text field (the input box itself) ---
		datePickerSettings.setColor(DatePickerSettings.DateArea.TextFieldBackgroundValidDate,      BG_PANEL);
		datePickerSettings.setColor(DatePickerSettings.DateArea.TextFieldBackgroundInvalidDate,    new Color(0x3A, 0x1E, 0x1E));
		datePickerSettings.setColor(DatePickerSettings.DateArea.TextFieldBackgroundDisabled,   BG_NAV);
		datePickerSettings.setColor(DatePickerSettings.DateArea.DatePickerTextValidDate,           TEXT_PRIMARY);
		datePickerSettings.setColor(DatePickerSettings.DateArea.DatePickerTextInvalidDate,         new Color(0xFF, 0x6B, 0x6B));
		datePickerSettings.setColor(DatePickerSettings.DateArea.DatePickerTextDisabled,        TEXT_DISABLED);

		// --- Fonts (keep readable at any size) ---
		Font uiFont = new Font("Segoe UI", Font.PLAIN, 13);
		Font boldFont = new Font("Segoe UI", Font.BOLD, 13);
		datePickerSettings.setFontValidDate(uiFont);
		datePickerSettings.setFontInvalidDate(uiFont);
		datePickerSettings.setFontVetoedDate(uiFont);
		datePickerSettings.setFontCalendarDateLabels(uiFont);
		datePickerSettings.setFontCalendarWeekdayLabels(boldFont);
		datePickerSettings.setFontCalendarWeekNumberLabels(uiFont);
		datePickerSettings.setFontMonthAndYearMenuLabels(boldFont);
		datePickerSettings.setFontMonthAndYearNavigationButtons(boldFont);
		datePickerSettings.setFontTodayLabel(uiFont);
		datePickerSettings.setFontClearLabel(uiFont);
		
	}

	@Override
	public void setDateTimePermissive(LocalDateTime optionalDateTime) {
		if(optionalDateTime != null && optionalDateTime.isBefore(LocalDateTime.now())) {
			super.setDateTimePermissive(LocalDateTime.now().plusMinutes(1));
			return;
		}
		super.setDateTimePermissive(optionalDateTime);
	}
	
	

}
