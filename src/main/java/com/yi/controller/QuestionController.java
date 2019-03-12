package com.yi.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.extensions.XSSFCellAlignment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.yi.domain.Criteria;
import com.yi.domain.PageMaker;
import com.yi.domain.QuestionVO;
import com.yi.service.QuestionService;
import com.yi.util.MediaUtils;

@Controller
@RequestMapping("/question/*")
public class QuestionController {

	private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);

	@Autowired
	private QuestionService service;
	@Autowired
	private DataSource ds;
	// �⺻���� ���Թ������� servlet-context�� ��ϵ� �̸����� ����
	@Resource(name = "uploadPath")
	private String uploadPath;

	@RequestMapping(value = "register", method = RequestMethod.GET)
	public void registerGet() {
		logger.info("QuestionVO create------------GET");
	}

	// �����߰�
	@RequestMapping(value = "register", method = RequestMethod.POST)
	public String registerPost(QuestionVO vo, String number, MultipartFile pictureFile) {
		logger.info("QuestionVO create------------POST");
		// �����, ���´� �Է¾��� �⺻ 0��/����ó��
		vo.setState("����");
		vo.setCorrectRate(0);
		// �������� ��� ����
		String picture = pictureFile.getOriginalFilename();
		if (picture.equals("") == false) {
			System.out.println("=============pictureFile.getOriginalFilename()" + picture);
			vo.setPicture(uploadPath + "/" + picture);
			logger.info("QuestionVO create------------" + vo);
		}
		// ����/ȸ��/��ȣ�� �̿��Ͽ� Code�Է��ϱ�
		String threeNum = String.format("%03d", Integer.parseInt(number));
		System.out.println("Q" + vo.getSubject() + vo.getYear() + vo.getRound() + threeNum);
		vo.setQuestionCode("Q" + vo.getSubject() + vo.getYear() + vo.getRound() + threeNum);
		// insert����
		service.insert(vo);
		System.out.println(vo);

		return "redirect:/question/register";
	}

	// �����߰�
	@RequestMapping(value = "registerfile", method = RequestMethod.GET)
	public void registerfileGet() {
		logger.info("registerfileGet------------GET");
	}

	// �����߰� ���� ���ε�, upload���Ͽ� ���ϸ��� �ø� �� �ø����Ϸ� insert ����, ���� ���� ����
	@RequestMapping(value = "registerfile", method = RequestMethod.POST)
	public void registerfilePost(MultipartFile file, HttpServletRequest req) throws IOException {
		logger.info("registerfilePost------------Post");
		logger.info("registerfilePost------------filePath-" + file.getOriginalFilename());// ���� �޾ƿ�
		// ���� upload������ ����
		String uploadPath2 = req.getRealPath("resources/upload");// ������ ����� upload������ ���
		logger.info("req.getRealPath(upload)------------" + uploadPath2);

		File dir = new File(uploadPath2);
		if (dir.exists() == false) {// ���ε� ������ ������ �����������(�Ϸ�)
			dir.mkdirs();
		}

		// ���� ���� �� ����
		InputStream inStream = null;
		OutputStream outStream = null;

		try {
			inStream = file.getInputStream(); // ��������
			logger.info("file.getOriginalFilename()------------" + file.getInputStream());
			outStream = new FileOutputStream(uploadPath2 + "/" + file.getOriginalFilename()); // �̵���ų ��ġ
			logger.info("uploadPath2+file.getOriginalFilename()------------" + uploadPath2 + file);

			byte[] buffer = new byte[1024];
			int length;

			while ((length = inStream.read(buffer)) > 0) {
				outStream.write(buffer, 0, length);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			outStream.close();
		}

		// LOAD DATA�� �̿��� insert�κ�
		/*
		 * try (Connection con = ds.getConnection(); Statement stmt =
		 * con.createStatement()) { String tableName = "question"; //String filePath =
		 * "C:/ProgramData/MySQL/MySQL Server 5.7/Uploads/question201901_5.csv"; String
		 * sql = String.format("LOAD DATA LOCAL INFILE '%s' IGNORE INTO TABLE %s " +
		 * "character set 'UTF8' " + "fields TERMINATED by ',' ENCLOSED BY '\"' " +
		 * "LINES TERMINATED BY '\\r\\n' " + "IGNORE 1 lines " +
		 * "(question_code, question_title, choice1, choice2, choice3, choice4, correct, state, correct_rate, picture,`year`,round, subject)"
		 * ,uploadPath2+file.getOriginalFilename(), "question");
		 * 
		 * stmt.executeQuery(sql); System.out.println(sql);
		 * 
		 * } catch (SQLException e) { e.printStackTrace(); }
		 */

		// poi ���̺귯���� �̿�
		List<QuestionVO> list = new ArrayList<>();

		FileInputStream fis = null;
		XSSFWorkbook workbook = null;

		try {
			fis = new FileInputStream(uploadPath2 + "\\" + file.getOriginalFilename());
			logger.info("====uploadPath2+file.getOriginalFilename()------------" + uploadPath2 + "\\"
					+ file.getOriginalFilename());
			workbook = new XSSFWorkbook(fis);

			// Ž���� ����� Sheet, Row, Cell ��ü
			XSSFSheet curSheet;
			XSSFRow curRow;
			XSSFCell curCell;
			QuestionVO vo;

			// Sheet Ž�� for��
			for (int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) {
				// ���� Sheet ��ȯ
				curSheet = workbook.getSheetAt(sheetIndex);
				// row Ž�� for��
				for (int rowIndex = 0; rowIndex < curSheet.getPhysicalNumberOfRows(); rowIndex++) {
					// row 0�� ��������̱� ������ ����
					if (rowIndex != 0) {
						// ���� row ��ȯ
						curRow = curSheet.getRow(rowIndex);
						vo = new QuestionVO();

						// row�� ù��° cell���� ������� ���� ��� �� cellŽ��
						if (!"".equals(curRow.getCell(0).getStringCellValue())) {
							// cell Ž�� for ��
							for (int cellIndex = 0; cellIndex < curRow.getPhysicalNumberOfCells()+1; cellIndex++) {
								curCell = curRow.getCell(cellIndex);

								switch (cellIndex) {
								case 0: // �����ڵ�
									vo.setQuestionCode(getCellValue(curCell));
									break;
								case 1: // title
									vo.setQuestionTitle(getCellValue(curCell));
									break;
								case 2: // ����1
									vo.setChoice1(getCellValue(curCell));
									break;
								case 3: // ����2
									vo.setChoice2(getCellValue(curCell));
									break;
								case 4: // ����3
									vo.setChoice3(getCellValue(curCell));
									break;
								case 5: // ����4
									vo.setChoice4(getCellValue(curCell));
									break;
								case 6: // ����
									vo.setCorrect(Integer.parseInt(getCellValue(curCell).substring(0, 1)));
									break;
								case 7: // ����
									vo.setState(getCellValue(curCell));
									break;
								case 8: // �����
									vo.setCorrectRate(Integer.parseInt(getCellValue(curCell).substring(0, 1)));
									break;
								case 9: // ����
									vo.setPicture(getCellValue(curCell));
									break;
								case 10: // ����
									vo.setYear(Integer.parseInt(getCellValue(curCell).substring(0, 4)));
									break;
								case 11: // ȸ��
									vo.setRound(Integer.parseInt(getCellValue(curCell).substring(0, 1)));
									break;
								case 12: // ����
									vo.setSubject(getCellValue(curCell));
									break;
								default:
									break;
								}

							}
							// cell Ž�� ���� vo �߰�
							list.add(vo);
						}
					}
				}

			}
			logger.info("�������Ϸ� �о�帰 list------------" + list);
			// list�� insert�ϱ�
			for(int i=0;i<list.size();i++) {
				service.insert(list.get(i));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		// insert�Ϸ� �� upload������ ����ƴ� ���� ����
		File delFile = new File(uploadPath2 + "/" + file.getOriginalFilename());
		delFile.delete();
	}

	private String getCellValue(XSSFCell cell) {
		logger.info("---value -------------" + cell);
		String value;
		try {
			value = cell.toString();
		}catch(NullPointerException e) {
			value = "";
		}
		return value;
	}
	
	// jsp�� ���� �޼ҵ�
	@RequestMapping(value = "moketest", method = RequestMethod.GET)
	public void moketest(Criteria cri, Model model) {
		System.out.println("cri===========:" + cri);
		List<QuestionVO> list = service.selectByYearAndRound(2018, 3, cri);/////// ������ ȸ�� �ܺο��� �ޱ�!!!!

		PageMaker pageMaker = new PageMaker();
		pageMaker.setCri(cri);
		pageMaker.setTotalCount(service.totalCount(2018, 3));

		model.addAttribute("list", list);
		model.addAttribute("pageMaker", pageMaker);
		model.addAttribute("cri", cri);
	}

	// jsp�� ���� �޼ҵ�
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public void list(Criteria cri, Model model) {
		System.out.println("cri===========:" + cri);
		List<QuestionVO> list = service.selectByYearAndRound(2018, 3, cri);/////// ������ ȸ�� �ܺο��� �ޱ�!!!!

		PageMaker pageMaker = new PageMaker();
		pageMaker.setCri(cri);
		pageMaker.setTotalCount(service.totalCount(2018, 3));

		model.addAttribute("list", list);
		model.addAttribute("pageMaker", pageMaker);
		model.addAttribute("cri", cri);
	}

	// �������� �ѹ�����
	@RequestMapping(value = "singletest", method = RequestMethod.GET)
	public void singleTest(Criteria cri, Model model) {
		System.out.println("cri===========:" + cri);
		cri.setPerPageNum(1);
		List<QuestionVO> list = service.selectByRandom();
		model.addAttribute("list", list);
		model.addAttribute("cri", cri);
	}

	// ���� ���� : �������� �� �ٷ� insert�Ǵ� controller
	@RequestMapping(value = "subjecttest", method = RequestMethod.GET)
	public void subjecttest(Criteria cri, Model model) {
		logger.info("subjecttest get------------");
		List<QuestionVO> list = service.selectBySubject("D");// �ܺο��� �� �ޱ�!

		PageMaker pageMaker = new PageMaker();
		cri.setPerPageNum(1);
		pageMaker.setCri(cri);
		pageMaker.setTotalCount(service.totalCount(2018, 3));

		model.addAttribute("list", list);
		model.addAttribute("pageMaker", pageMaker);
		model.addAttribute("cri", cri);
	}

	// json�� ������ �޼ҵ� - list.jsp���� ���
	@ResponseBody
	@RequestMapping(value = "listJson/{year}/{round}", method = RequestMethod.GET)
	public ResponseEntity<List<QuestionVO>> listJson(Criteria cri, @PathVariable("year") int year,
			@PathVariable("round") int round) {
		ResponseEntity<List<QuestionVO>> entity = null;

		try {
			List<QuestionVO> list = service.selectByYearAndRound(year, round, cri);
			entity = new ResponseEntity<>(list, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			entity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);// List<QuestionVO>�� ������ �ϳ�, ��������������HttpStatus�� ����
		}

		return entity;
	}

	// page�� ajax�� ó���ϴ� list
	@ResponseBody
	@RequestMapping(value = "listJson/{year}/{round}/{page}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> listJsonPage(Criteria cri, @PathVariable("year") int year,
			@PathVariable("round") int round, @PathVariable("page") int page) {
		ResponseEntity<Map<String, Object>> entity = null;

		try {
			cri.setPage(page);
			List<QuestionVO> list = service.selectByYearAndRound(year, round, cri);
			// ����������
			PageMaker pageMaker = new PageMaker();
			pageMaker.setCri(cri);
			// ����Ʈ������ ������������ ���� ���������� map�� ����
			HashMap<String, Object> map = new HashMap<>();
			map.put("list", list);
			map.put("pageMaker", pageMaker);
			entity = new ResponseEntity<>(map, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			entity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);// List<QuestionVO>�� ������ �ϳ�, ��������������HttpStatus�� ����
		}

		return entity;
	}

	// json���� ���� ����select ������ �޼ҵ� - subjecttest.jsp���� ���
	@ResponseBody
	@RequestMapping(value = "subjecttest/{subject}", method = RequestMethod.GET)
	public ResponseEntity<List<QuestionVO>> listBySubjectJson(@PathVariable("subject") String subject) {
		ResponseEntity<List<QuestionVO>> entity = null;

		try {
			List<QuestionVO> list = service.selectBySubject(subject);
			entity = new ResponseEntity<>(list, HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			entity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		return entity;
	}

	// �����ڰ� ���� ����â���� �̵��ϴ� ��
	@RequestMapping(value = "modify", method = RequestMethod.GET)
	public void updateget(String questionCode, Model model) {
		ResponseEntity<String> entity = null;
		QuestionVO vo = new QuestionVO();
		vo.setQuestionCode(questionCode);
		vo = service.selectByNO(vo);

		model.addAttribute("vo", vo);
	}

	@RequestMapping(value = "{questionCode}", method = RequestMethod.PUT)
	public ResponseEntity<String> update(@PathVariable("questionCode") String questionCode,
			@RequestBody QuestionVO vo) {
		ResponseEntity<String> entity = null;
		try {
			vo.setQuestionCode(questionCode);
			service.update(vo);
			entity = new ResponseEntity<String>("success", HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			entity = new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		return entity;
	}

	@RequestMapping(value = "{questionCode}", method = RequestMethod.DELETE)
	public ResponseEntity<String> remove(@PathVariable("questionCode") String questionCode) {
		ResponseEntity<String> entity = null;

		try {
			QuestionVO vo = new QuestionVO();
			vo.setQuestionCode(questionCode);
			service.delete(vo);
			entity = new ResponseEntity<String>("success", HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			entity = new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		return entity;
	}

	@ResponseBody
	@RequestMapping("/displayFile")
	public ResponseEntity<byte[]> displayFile(String filename, Model model) {
		ResponseEntity<byte[]> entity = null;
		logger.info("displayFile : " + filename);
		try {
			try {
				String format = filename.substring(filename.lastIndexOf(".") + 1);
				String picture = filename.substring(filename.lastIndexOf("/"));
				MediaType mType = MediaUtils.getMediaType(format);

				HttpHeaders headers = new HttpHeaders();
				InputStream in = null;
				in = new FileInputStream(uploadPath + "/" + picture);
				headers.setContentType(mType);

				entity = new ResponseEntity<byte[]>(IOUtils.toByteArray(in), headers, HttpStatus.CREATED);
				in.close();
			} catch (StringIndexOutOfBoundsException e) {
				return null;
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			entity = new ResponseEntity<byte[]>(HttpStatus.BAD_REQUEST);
		}
		return entity;
	}

}
