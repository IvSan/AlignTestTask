package xyz.hardliner.align.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import xyz.hardliner.align.domain.Product;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.IntStream;

@Slf4j
@Service
@SuppressWarnings("WeakerAccess")
public class XlsGenerator {

	private static final String SHEET_NAME = "Products";

	public static String fileName() {
		return "Products_" + DateTimeFormatter.ofPattern("yyyy_MM_dd_HH:mm").format(LocalDateTime.now()) + ".xls";
	}

	public byte[] create(List<Product> products) {
		try (Workbook workbook = new XSSFWorkbook()) {
			workbook.createSheet(SHEET_NAME);
			createHeaderRow(workbook);
			createDataRows(workbook, products);
			autoSizeColumns(workbook);
			try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
				workbook.write(bos);
				return bos.toByteArray();
			}
		} catch (IOException e) {
			log.error("Cannot create XLS export file: ", e);
			throw new IllegalStateException("Failed XLS export");
		}
	}

	private CellStyle headerCellStyle(Workbook workbook) {
		Font headerFont = workbook.createFont();
		headerFont.setBold(true);

		CellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setFont(headerFont);
		headerCellStyle.setAlignment(HorizontalAlignment.CENTER);

		return headerCellStyle;
	}

	private void createHeaderRow(Workbook workbook) {
		Sheet sheet = workbook.getSheet(SHEET_NAME);
		CellStyle headerCellStyle = headerCellStyle(workbook);
		Row headerRow = sheet.createRow(0);
		String[] headers = new String[]{"Id", "Name", "Brand", "Price", "Quantity"};
		int cellIndex = 0;
		for (String header : headers) {
			Cell cell = headerRow.createCell(cellIndex++);
			cell.setCellValue(header);
			cell.setCellStyle(headerCellStyle);
		}
	}

	private CellStyle dataCellStyle(Workbook workbook) {
		CellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
		return headerCellStyle;
	}

	private void createDataRows(Workbook workbook, List<Product> products) {
		Sheet sheet = workbook.getSheet(SHEET_NAME);
		CellStyle dataCellStyle = dataCellStyle(workbook);
		int rowIndex = 1;
		for (Product product : products) {
			Row dataRow = sheet.createRow(rowIndex++);
			dataRow.createCell(0).setCellValue(product.getId());
			dataRow.createCell(1).setCellValue(product.getName());
			dataRow.createCell(2).setCellValue(product.getBrand());
			dataRow.createCell(3).setCellValue(product.getPrice());
			dataRow.createCell(4).setCellValue(product.getQuantity());
			dataRow.setRowStyle(dataCellStyle);
		}
	}

	private void autoSizeColumns(Workbook workbook) {
		Sheet sheet = workbook.getSheet(SHEET_NAME);
		IntStream.range(0, 5).forEach(sheet::autoSizeColumn);
	}
}
