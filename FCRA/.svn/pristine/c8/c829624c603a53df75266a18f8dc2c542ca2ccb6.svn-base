package utilities;

import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import models.master.Office;
import utilities.DatabaseConnection;
import utilities.lists.*;
import utilities.notifications.*;
import utilities.UserSession;

public class Commons {
	// START - COMMON FOR ALL
	public HttpSession httpSession;
	//public SessionMap session;
	protected String sessionId;
	public Connection connection;
	private boolean isGtt;
	public PreparedStatement statement;
	public ResultSet resultSet;
	public StringBuffer query;
	public StringBuffer queryFields;
	public StringBuffer queryFields_1;
	public String myDate;
	public String myDateTime;
	protected String istDateTime;
	public long myDateTimeL;
	public long istDateTimeL;
	public DateFormat dateFormatSmall;
	public Calendar istCalendar;
	public Calendar localCalendar;
	public long istOffset;
	public long localOffset;
	public SimpleDateFormat dateFormat;
	public SimpleDateFormat dateTimeFormat;
	private TimeZone tz;
	public final TimeZone istTZ=TimeZone.getTimeZone("Asia/Calcutta");
	public Office myOffice;
	public UserSession myUser;
	public String myUserId;
	public String myUserName;
	public String myUserDesignation;
	public String myUserDesignationId;
	public String myOfficeId;
	public String myOfficeCode;
	public String myCountryCode;
	public String resultType;
	public String nullValue;
	public String myIpAddress;
	private StringBuffer searchField;
	private StringBuffer searchValue;
	private boolean statusCheck;
	private int updateCount;
	private StringBuffer statusOf;
	private StringBuffer statusValue;
	public Type type;
	public String statusName;
	private StringBuffer customize;
	private String customizeString;
	public StopWatch s = new StopWatch();
	public StringBuffer table;
	private StringBuffer rows;
	private StringBuffer rowHtml;
	private StringBuffer rowTag;
	private StringBuffer rowClass;
	private StringBuffer colHtml;
	private StringBuffer colTag;
	private String flag1;
	private String flag2;
	private String docFlag;
	protected String dbUserId;
	public Exception exception;
	private boolean autoCommit;
	public List<List1> list1 = new ArrayList<List1>();
	public List<List2> list2 = new ArrayList<List2>();
	public List<List3> list3 = new ArrayList<List3>();
	public List<List4> list4 = new ArrayList<List4>();
	public List<List5> list5 = new ArrayList<List5>();
	public List<String> queryList = new ArrayList<String>();
	public List<String> fileMimeList = new ArrayList<String>();
	public List<String> fileNameList = new ArrayList<String>();
	public List<byte[]> fileBytesList = new ArrayList<byte[]>();
	public List<String[]> queryValuesList = new ArrayList<String[]>();
	public List<Integer> updateCountList = new ArrayList<Integer>();
	private StringBuffer[] queryValues;
	private StringBuffer[] resultFields;
	public List<String[]> resultFieldsList = new ArrayList<String[]>();

	// Start - With Getters, Setters
	public String processTime;
	public String applicationId;
	public String functionFlag;
	public String requestType;
	public String responseType;
	public List<Notification> notifyList = new ArrayList<Notification>();
	// End - With Getters, Setters
	
	
	public static boolean nob(String s) { // Check if null or blank
		return (s == null || s.trim().equals(""));
	}
	public void finish() { // kill current connection and evaluate process time
		kc();
		eta();
	}
	private void eta() { // Calculate Process Time
		s.stop();
		processTime = String.valueOf(s.getElapsedTimeSecs());
	}
	public StringBuffer nc(String value) { // NULL CHECK
		return new StringBuffer((nob(value) ? "" : value));
	}
	private StringBuffer nct(String value) { // NULL CHECK return title
		return new StringBuffer((nob(value) ? "title=\"Not Available\"" : ""));
	}
	public StringBuffer ncl(StringBuffer label, StringBuffer value) { // return title in font tag
		if (nob(value.toString())) {
			label = new StringBuffer("<font title=\"Not Available\">" + label
					+ "</font>");
		}
		return label;
	}
	public String ncl(String label, String value) { // return title in font tag
		if (nob(value)) {
			label = new String("<font title=\"Not Available\"></font>");
		}
		return label;
	}
	private StringBuffer ync(StringBuffer value) { // Y/N CHECK
		if (("Y").contentEquals(value)) {
			value = new StringBuffer("Yes");
		} else if (("N").contentEquals(value)) {
			value = new StringBuffer("No");
		}
		return value;
	}
	protected void initAll() throws Exception{ // Initialize general variables
		initUser();				
		initParams();
	}
	private void initUser() throws Exception{
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
	    httpSession = attr.getRequest().getSession(true);
		//session=(SessionMap)httpSession.g;
		sessionId=httpSession.getId();//ServletActionContext.getRequest().getSession().getId();
		myUser = (UserSession)httpSession.getAttribute("user");//(UserSession)session.get("user");
		TimeZone.setDefault(istTZ);
		tz=TimeZone.getDefault();
		if(myUser!=null){
			myIpAddress = myUser.getIp();
			myUserId = myUser.getUserId();
			myUserName=myUser.getUserName();
			myUserDesignation=myUser.getUserDesignation();
			myUserDesignationId=myUser.getUserDesignationId();
			myOffice = myUser.getOffice();
			myOfficeId = myOffice.getOfficeType().getOfficeId().toString();
			myCountryCode = myOffice.getCountry().getCountryCode();
			myOfficeCode = myOffice.getOfficeCode();
			tz = TimeZone.getTimeZone(myUser.getOffice().getTimeZone().getZoneName());
		}
	}
	private void initParams() throws Exception{
		/*KINDLY DON'T CHANGE THE ORDER OF BELOW STATEMENTS AT ALL*/
		dbUserId=DatabaseConnection.rootUser+".";
		initDates();
		//Example for SQL side in place of sysdate to_date(myDateTime,'dd-mm-yyyy HH24:MI:SS')
		/*KINDLY DON'T CHANGE THE ORDER OF BELOW STATEMENTS AT ALL*/
		
		// nullValue="<font class=\"text-error\" title=\"Not Available\">N.A.</font>";
		nullValue = "<font class=\"text-error\" title=\"Not Available\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font>";
	}
	private void initDates(){
		istCalendar=Calendar.getInstance(istTZ);
		localCalendar=Calendar.getInstance(tz);
		dateFormat = new SimpleDateFormat("dd-mm-yyyy");
		dateTimeFormat = new SimpleDateFormat("dd-mm-yyyy HH:mm:ss");
		initLocalDate();
		initISTDate();
	}
	private void initLocalDate(){
		localOffset=tz.getRawOffset();
		spl(pullDateTime(tz));
		localCalendar.setTimeInMillis(pullDateTime(tz));
		//dateFormat.setTimeZone(tz);
		myDate = dateFormat.format(localCalendar.getTime());
		myDateTime = dateTimeFormat.format(localCalendar.getTime());
		myDateTimeL=localCalendar.getTimeInMillis();
		spl("Local Time 11111: "+myDate);
	}
	private void initISTDate(){
		istOffset=istTZ.getRawOffset();
		//istCalendar.setTimeZone(istTZ);
		istCalendar.setTimeInMillis(pullDateTime(istTZ));
		istDateTimeL = istCalendar.getTimeInMillis();
		istDateTime=dateTimeFormat.format(istCalendar.getTime());
		spl("IST Time : "+istDateTimeL);
	}
	private long pullDateTime(TimeZone zone){
		Calendar cSchedStartCal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		long gmtTime = cSchedStartCal.getTimeInMillis();
		long timezoneAlteredTime = gmtTime + zone.getRawOffset();//- istTZ.getRawOffset();
		return timezoneAlteredTime;
	}
	protected Calendar pullCalendar(int timezoneCode) throws Exception{
		StringBuffer query=new StringBuffer("SELECT SYSTIMESTAMP AT TIME ZONE (SELECT ZONE_NAME FROM T_TIMEZONE_MASTER WHERE ZONE_ID="+timezoneCode+") as time, (SELECT ZONE_NAME FROM T_TIMEZONE_MASTER WHERE ZONE_ID="+timezoneCode+") as timezone from dual");
		statement=connection.prepareStatement(query.toString());
		resultSet=statement.executeQuery();
		Calendar cal;
		if(resultSet.next()){
			cal=Calendar.getInstance(TimeZone.getTimeZone(resultSet.getString(2)));
			cal.setTime(resultSet.getDate(1));
		}
		else{
			throw new Exception("Not able to construct a valid date for the specified timezone");
		}
		return cal;
	}
	private void kc() { // Kill current connection
		//DatabaseConnection.dropConnection(connection);
		try {
			resultSet.close();
		} catch (Exception e) {
		}
		try {
			statement.close();
		} catch (Exception e) {
		}
		try {
			connection.commit();
		} catch (Exception e) {
		}
		try {
			connection.close();
		} catch (Exception e) {
		}
		if(myUser!=null && myUser.getGtt() && isGtt){
			try {
				//session.remove("user");
				httpSession.removeAttribute("user");
				myUser.setGtt(false);
				myUser.getConnection().close();
			} catch (Exception e) {
			}finally{
				try{
					myUser.setConnection(null);
				}catch(Exception e1){
				}
				//session.put("user", myUser);
				httpSession.setAttribute("user", myUser);
			}
		}
		// run.gc();
	}
	protected void mc(boolean inJboss) throws Exception{ // Make a new connection
		s.start();
		connection = DatabaseConnection.getConnection(inJboss);
	}
	private void mc(boolean inJboss, String dbUserId, String dbPassword) throws Exception{ // Make a new connection with specified dbUserId
		s.start();
		connection = DatabaseConnection.getConnection(inJboss, dbUserId);
	}
	private void saveConnection(){
		//session.remove("user");
		httpSession.removeAttribute("user");
		myUser.setConnection(connection);
		myUser.setGtt(isGtt);
		//session.put("user", myUser);
		httpSession.setAttribute("user", myUser);
	}
	protected void begin() { // Makes connection to root & initialises various parameters for normally logged in user
		begin(true);
	}
	protected void begin(boolean inJboss) { // Makes connection to root & initialises various parameters for normally logged in user
		try {
			mc(inJboss);
			initAll();
		} catch (Exception e) {
			//ps(e);
		}
	}
	protected void beginGtt(boolean fromSession) {  // Makes connection via object stored in session(based on "fromSession" flag) & initialises various parameters for normally logged in user
		try {
			isGtt=true;
			if(fromSession){
				initAll();
				s.start();
				connection = myUser.getConnection();
			}
			else{
				begin();
				connection.setAutoCommit(false);
			}
		} catch (Exception e) {
			//ps(e);
		}
		finally{
			if(!fromSession){
				saveConnection();
			}
		}
	}
	protected void beginGtt(boolean fromSession, String dbUserId, String dbPassword) {  // Makes connection via object stored in session(based on "fromSession" flag) & initialises various parameters for normally logged in user
		try {
			isGtt=true;
			if(fromSession){
				initAll();
				s.start();
				connection = myUser.getConnection();
			}
			else{
				begin(dbUserId, dbPassword);
				connection.setAutoCommit(false);
			}
		} catch (Exception e) {
			//ps(e);
		}
		finally{
			if(!fromSession){
				saveConnection();
			}
		}
	}
	protected void beginStealth(boolean inJboss) {// Makes connection to root for server's background processes
		try {
			mc(inJboss);
			initParams();
		} catch (Exception e) {
			//ps(e);
		}
	}
	protected void beginStealth() {// Makes connection to root for server's background processes
		beginStealth(true);
	}
	protected void begin(boolean inJboss, String dbUserId, String dbPassword) {// Makes connection to specified dbUserId & initialises various parameters for normally logged in user
		try {
			mc(inJboss, dbUserId, dbPassword);
			initAll();
		} catch (Exception e) {
			//ps(e);
		}
	}
	protected void begin(String dbUserId, String dbPassword) {// Makes connection to specified dbUserId & initialises various parameters for normally logged in user
		begin(true,dbUserId,dbPassword);
	}
	protected void beginStealth(boolean inJboss, String dbUserId, String dbPassword) {// Makes connection to specified dbUserId for server's background processes
		try {
			mc(inJboss, dbUserId, dbPassword);
			initParams();
		} catch (Exception e) {
			//ps(e);
		}
	}
	protected void beginStealth(String dbUserId, String dbPassword) {// Makes connection to specified dbUserId for server's background processes
		beginStealth(true, dbUserId, dbPassword);
	}
	public void sp(Object o) { // Print text without next line
		System.out.print(o);
		System.out.flush();
	}
	public void spl(Object o) { // Print text with next line
		//sp("\n" + o);
		System.out.println(o);
		System.out.flush();
	}
	public void spl() {
		//sp("\n");
		System.out.println();
	}
	public void ps(Exception e) { // Print stack trace
		//sp("\n");
		System.out.println();
		e.printStackTrace();
	}
	public StringBuffer sb(String s) {
		return new StringBuffer(s);
	}
	private void evalStatus(int[] u) {
		boolean failure = false;
		boolean success = false;
		for (int i = 0; i < u.length; i++) {
			if (u[i] == 0)
				failure = true;
			else if (u[i] > 0)
				success = true;
		}
		if (success) {
			if (failure)
				statusName = "some";
			else
				statusName = "all";
		} else {
			statusName = "none";
		}
	}
	private void initQueryValues(String[] queryValues) {
		this.queryValues = new StringBuffer[queryValues.length];
		for (int i = 0; i < queryValues.length; i++) {
			this.queryValues[i] = nc(queryValues[i]);
		}
	}
	private void initResultFields(String[] resultFields) {
		this.resultFields = new StringBuffer[resultFields.length];
		for (int i = 0; i < resultFields.length; i++) {
			this.resultFields[i] = nc(resultFields[i]);
		}
	}
	private void pushStatus() { // Don't make it other than private
		try {
			if (statusCheck) {
				if (statusName.equals("all")) {
					notifyList.add(new Notification("Success!", statusOf
							+ " is/are successfully " + statusValue + ".",
							Status.SUCCESS, type));
				} else {
					if (statusName.equals("none"))
						notifyList.add(new Notification("Error!", statusOf
								+ " could not be " + statusValue
								+ " at all. Please try later.", Status.ERROR,
								type));
					else if (statusName.equals("some"))
						notifyList.add(new Notification("Error!", statusOf
										+ " could not be completely "
										+ statusValue + ". Please try later.",
										Status.ERROR, type));
					else
						errorStatus();
				}
			}
			if(!isGtt){
				if (statusName.equals("all")) {
					connection.commit();
				} else {
					connection.rollback();
				}
				connection.setAutoCommit(autoCommit);
			}
		} catch (Exception e) {
		}
	}
	public void pushStatus(boolean statusCheck, String statusName,
			String statusOf, String statusValue) throws NotificationException {
		if (statusCheck) {
			if (statusName.equals("all")) {
				notifyList.add(new Notification("Success!", statusOf
						+ " is/are successfully " + statusValue + ".",
						Status.SUCCESS, type));
			} else {
				if (statusName.equals("none"))
					notifyList.add(new Notification("Error!", statusOf
							+ " could not be " + statusValue
							+ " at all. Please try later.", Status.ERROR,
							type));
				else if (statusName.equals("some"))
					notifyList.add(new Notification("Error!", statusOf
							+ " could not be completely " + statusValue
							+ ". Please try later.", Status.WARNING, type));
				else
					errorStatus();
			}
		}
	}
	private void pullStatus() throws NotificationException {
		if (statusCheck) {
			if (statusName.equals("all")) {
			} else if (statusName.equals("none")) {
				notifyList.add(new Notification("Info!", "No data found for "
						+ searchField + " \"" + searchValue + "\" .",
						Status.ERROR, type));
			} else if (statusName.equals("some")) {
				notifyList.add(new Notification("Error!",
						"Some data not found for " + searchField + " \""
								+ searchValue + "\" .", Status.WARNING,
								type));
			} else {
				errorStatus();
			}
		}
	}
	public void pullStatus(boolean statusCheck, String statusName,
			String searchField, String searchValue) throws NotificationException {
		if (statusCheck) {
			if (statusName.equals("all")) {
			} else if (statusName.equals("none")) {
				notifyList.add(new Notification("Info!", "No data found for "
						+ searchField + " \"" + searchValue + "\" .",
						Status.ERROR, type));
			} else if (statusName.equals("some")) {
				notifyList.add(new Notification("Error!",
						"Some data not found for " + searchField + " \""
								+ searchValue + "\" .", Status.WARNING,
								type));
			} else {
				errorStatus();
			}
		}
	}
	private void errorStatus() throws NotificationException {
		notifyList.add(new Notification("Error!",
				"Some unexpected error occured. Please try later.",
				Status.ERROR, type));
	}
	public void errorStatus(Type type) {
		try{
			notifyList.add(new Notification("Error!","Some unexpected error occured. Please try later.",Status.ERROR, type));
		}catch(NotificationException ne){
			ps(ne);
		}
	}
	public void errorStatus(Type type,String location) {
		try{
			notifyList.add(new Notification("Error!","Some unexpected error occured. Please try later.",Status.ERROR, type,location));
		}catch(NotificationException ne){
			ps(ne);
		}
	}
	public void errorStatus(Type type, int ms) throws NotificationException {
		notifyList.add(new Notification("Error!", "Some unexpected error occured. Please try later.", Status.ERROR, type, Closeable.TRUE, ms));
	}
	private void initPush(int updateCount, String statusOf, String statusValue,
			String customize, boolean statusCheck, Type type)
			throws Exception {
		this.statusOf = new StringBuffer(statusOf);
		this.statusValue = new StringBuffer(statusValue);
		this.customize = new StringBuffer(customize);
		this.statusCheck = statusCheck;
		this.updateCount = updateCount;
		this.type=type;
		if(!isGtt){
			autoCommit = connection.getAutoCommit();
			connection.setAutoCommit(false);
		}
	}

	private boolean doPush() throws Exception {
		statement = connection.prepareStatement(query.toString());
		for (int i = 0; i < queryValues.length; i++) {
			statement.setString((i + 1), queryValues[i].toString());
		}
		//spl(query);
		int c = statement.executeUpdate();
		if (c >= updateCount)
			return true;
		else
			return false;
	}

	public String push(int updateCount, String[] queryValues,
			String statusOf, String statusValue, String customize,
			boolean statusCheck, Type type) {
		try {
			initQueryValues(queryValues);
			initPush(updateCount, statusOf, statusValue, customize,
					statusCheck, type);
			statusName = "none";
			if (doPush())
				statusName = "all";
		} catch (Exception e) {
			ps(e);
			exception = e;
			statusName = "error";
		}
		pushStatus();
		return statusName;
	}

	public String pushGroup(String statusOf, String statusValue,
			String customize, boolean statusCheck, Type location) {
		try {
			initPush(0, statusOf, statusValue, customize, statusCheck, location);
			int[] u = new int[queryList.size()];
			for (int i = 0; i < queryList.size(); i++) {
				u[i] = 0;
				query = new StringBuffer(queryList.get(i));
				initQueryValues(queryValuesList.get(i));
				updateCount = updateCountList.get(i);
				if (doPush())
					u[i] = 1;
			}
			evalStatus(u);
		} catch (Exception e) {
			ps(e);
			exception = e;
			statusName = "error";
		}
		pushStatus();
		queryList.clear();
		queryValuesList.clear();
		return statusName;
	}

	private void initPull(String searchField, String searchValue,
			String customize, boolean statusCheck, Type type)
			throws Exception {
		this.searchField = new StringBuffer(searchField);
		this.searchValue = new StringBuffer(searchValue);
		this.customize = new StringBuffer(customize);
		this.customizeString = (!nob(customize) ? customize : "");
		this.statusCheck = statusCheck;
		this.type = type;
	}

	private boolean doPullResultSet() throws Exception {
		statement = connection.prepareStatement(query.toString());
		for (int i = 0; i < queryValues.length; i++) {
			statement.setString((i + 1), queryValues[i].toString());
		}
		resultSet = statement.executeQuery();
		if (resultSet.next()) {
			return true;
		} else {
			return false;
		}
	}

	public String pullResultSet(String[] queryValues, String searchField,
			String searchValue, String customize, boolean statusCheck,
			Type location) throws NotificationException {
		try {
			initQueryValues(queryValues);
			initPull(searchField, searchValue, customize, statusCheck, location);
			statusName = "none";
			if (doPullResultSet())
				statusName = "all";
		} catch (Exception e) {
			ps(e);
			exception = e;
			statusName = "error";
		}
		pullStatus();
		return statusName;
	}

	private boolean doPull() throws Exception {
		responseType = "normal";
		statement = connection.prepareStatement(query.toString());
		for (int i = 0; i < queryValues.length; i++) {
			statement.setString((i + 1), queryValues[i].toString());
		}
		resultSet = statement.executeQuery();
		if (resultSet.next()) {
			for (int i = 0; i < resultFields.length; i++) {
				qa(resultFields[i], new StringBuffer(customField(0, (i + 1),
						nc(resultSet.getString(i + 1)).toString())));
			}
			return true;
		} else {
			return false;
		}
	}

	public String pull(String[] queryValues, String[] resultFields,
			String searchField, String searchValue, String customize,
			boolean statusCheck, Type location) throws NotificationException {
		try {
			list2.clear();
			initQueryValues(queryValues);
			initResultFields(resultFields);
			initPull(searchField, searchValue, customize, statusCheck, location);
			statusName = "none";
			if (doPull())
				statusName = "all";
		} catch (Exception e) {
			ps(e);
			exception = e;
			statusName = "error";
		}
		pullStatus();
		return statusName;
	}

	public String pullGroup(String searchField, String searchValue,
			String customize, boolean statusCheck, Type location) throws NotificationException {
		try {
			list2.clear();
			initPull(searchField, searchValue, customize, statusCheck, location);
			int[] u = new int[queryList.size()];
			for (int i = 0; i < queryList.size(); i++) {
				u[i] = 0;
				query = new StringBuffer(queryList.get(i));
				initQueryValues(queryValuesList.get(i));
				initResultFields(resultFieldsList.get(i));
				if (doPull())
					u[i] = 1;
			}
			evalStatus(u);
		} catch (Exception e) {
			ps(e);
			exception = e;
			statusName = "error";
		}
		pullStatus();
		queryList.clear();
		queryValuesList.clear();
		resultFieldsList.clear();
		return statusName;
	}

	private void clearLists(int c) {
		if (c == 1)
			list1.clear();
		if (c == 2)
			list2.clear();
		else if (c == 3)
			list3.clear();
		else if (c == 4)
			list4.clear();
		else if (c == 5)
			list5.clear();
	}

	private boolean doPullLists(int c) throws Exception {
		responseType = "normal";
		statement = connection.prepareStatement(query.toString());
		for (int i = 0; i < queryValues.length; i++) {
			statement.setString((i + 1), queryValues[i].toString());
		}
		resultSet = statement.executeQuery();
		int tracker = 0;
		while (resultSet.next()) {
			tracker++;
			if (c == 1) {
				qa(customField(tracker, 1, resultSet.getString(1)));
			}
			if (c == 2) {
				qa(customField(tracker, 1, resultSet.getString(1)),
						customField(tracker, 2, resultSet.getString(2)));
			} else if (c == 3) {
				qa(customField(tracker, 1, resultSet.getString(1)),
						customField(tracker, 2, resultSet.getString(2)),
						customField(tracker, 3, resultSet.getString(3)));
			} else if (c == 4) {
				qa(customField(tracker, 1, resultSet.getString(1)),
						customField(tracker, 2, resultSet.getString(2)),
						customField(tracker, 3, resultSet.getString(3)),
						customField(tracker, 4, resultSet.getString(4)));
			} else if (c == 5) {
				qa(customField(tracker, 1, resultSet.getString(1)),
						customField(tracker, 2, resultSet.getString(2)),
						customField(tracker, 3, resultSet.getString(3)),
						customField(tracker, 4, resultSet.getString(4)),
						customField(tracker, 5, resultSet.getString(5)));
			}
		}
		if (tracker == 0)
			return false;
		else
			return true;
	}

	public String pullLists(int c, String[] queryValues, String searchField,
			String searchValue, String customize, boolean statusCheck,
			Type location) throws NotificationException {
		try {
			clearLists(c);
			initQueryValues(queryValues);
			initPull(searchField, searchValue, customize, statusCheck, location);
			statusName = "none";
			if (doPullLists(c))
				statusName = "all";
		} catch (Exception e) {
			ps(e);
			exception = e;
			statusName = "error";
		}
		pullStatus();
		return statusName;
	}

	public String pullListsGroup(int c, String searchField,
			String searchValue, String customize, boolean statusCheck,
			Type location) throws NotificationException {
		try {
			clearLists(c);
			initPull(searchField, searchValue, customize, statusCheck, location);
			int[] u = new int[queryList.size()];
			for (int i = 0; i < queryList.size(); i++) {
				u[i] = 0;
				query = new StringBuffer(queryList.get(i));
				initQueryValues(queryValuesList.get(i));
				if (doPullLists(c))
					u[i] = 1;
			}
			evalStatus(u);
		} catch (Exception e) {
			ps(e);
			exception = e;
			statusName = "error";
		}
		pullStatus(); // statusCheck vl b taken for last grouped item - resolve
						// it...
		return statusName;
	}

	public String pullTableGroup(String title, boolean paged, int vrows,
			String[] resultFields, String searchField, String searchValue,
			String customize, boolean statusCheck, Type location) throws NotificationException {
		rows = new StringBuffer("");
		try {
			initResultFields(resultFields);
			initPull(searchField, searchValue, customize, statusCheck, location);
			int tracker = 0;
			int[] u = new int[queryList.size()];
			for (int i = 0; i < queryList.size(); i++) {
				u[i] = 0;
				query = new StringBuffer(queryList.get(i));
				initQueryValues(queryValuesList.get(i));
				int x = generateRows(paged, tracker);
				if (x > tracker) {
					tracker = x;
					u[i] = 1;
				}
			}
			generateTable(title, paged, vrows, tracker);
			evalStatus(u);
		} catch (Exception e) {
			ps(e);
			exception = e;
			statusName = "error";
		}
		queryList.clear();
		queryValuesList.clear();
		pullStatus();
		return statusName;
	}

	public String pullTablePage(String[] queryValues, String[] resultFields,
			int page, int rows, String searchField, String searchValue,
			String customize, boolean statusCheck, Type location) throws NotificationException {
		try {
			initQueryValues(queryValues);
			initResultFields(resultFields);
			initPull(searchField, searchValue, customize, statusCheck, location);
			queryFields = query;
			query = new StringBuffer(
					"SELECT * FROM (SELECT a.*, ROWNUM rnum FROM ");
			query.append("(" + queryFields + ")");
			query.append(" a where ROWNUM <= " + (((page - 1) * rows) + rows)
					+ " ) where rnum  >= " + (((page - 1) * rows) + 1) + "");
			statusName = "none";
			if (doPullTable("", false, 0, (page - 1) * rows))
				statusName = "all";
		} catch (Exception e) {
			ps(e);
			exception = e;
			statusName = "error";
		}
		pullStatus();
		return statusName;
	}

	public String pullTable(String title, boolean paged, int vrows,
			String[] queryValues, String[] resultFields, String searchField,
			String searchValue, String customize, boolean statusCheck,
			Type location) throws NotificationException {
		try {
			initQueryValues(queryValues);
			initResultFields(resultFields);
			initPull(searchField, searchValue, customize, statusCheck, location);
			statusName = "none";
			if (doPullTable(title, paged, vrows, 0))
				statusName = "all";
		} catch (Exception e) {
			ps(e);
			exception = e;
			statusName = "error";
		}
		pullStatus();
		return statusName;
	}

	private boolean doPullTable(String title, boolean paged, int vrows,
			int start) throws Exception {
		responseType = "table";
		rows = new StringBuffer("");
		int tracker = generateRows(paged, start);
		generateTable(title, paged, vrows, tracker);
		if (tracker > start)
			return true;
		else
			return false;
	}

	private void generateTable(String title, boolean paged, int vrows,
			int tracker) throws Exception {
		table = new StringBuffer("");
		if (tracker > 0) {
			if (tracker == 1) {
				responseType = "normal";
			}
			StringBuffer head = new StringBuffer("<th>#</th>");
			for (int i = 0; i < resultFields.length; i++) {
				head.append("<th>" + resultFields[i] + "</th>");
			}
			table = new StringBuffer(
					"<table id=\""
							+ customize
							+ "-table\" class=\"table tablesorter table-bordered table-condensed table-hover \">"
							+ "<thead><tr>" + head + "</tr></thead>"
							+ "<tbody>" + rows + "</tbody>" + "</table>");
			if (paged) {
				int rc = tracker;
				int pc = rc;
				if (rc < vrows && rc > 0) {
					pc = 1;
				} else if (rc >= vrows) {
					pc = rc / vrows;
					if ((rc % vrows) > 0) {
						pc++;
					}
				} else {
					pc = 1;
				}
				if (tracker > vrows) {
					StringBuffer pager = new StringBuffer("<div id=\""
							+ customize
							+ "-pager\"  theme=\"static\" paging=\"" + vrows
							+ "\" paging-for=\"" + customize
							+ "-table-div\" paging-limit=\"" + pc + "\"></div>");
					pager.append("<div id=\"" + customize + "-table-div\">");
					table.insert(0, pager);
					table
							.append("</div><script type=\"text/javascript\">doPaging('"
									+ customize + "-pager');</script>");
				}
				table.append("<script type=\"text/javascript\">doTableSorter('"
						+ customize + "-table');</script>");
			}
			if (!nob(title)) {
				table.insert(0, "<blockquote><strong class=\"text-error\">"
						+ title + "</strong></blockquote>");
			}
		}
	}

	private int generateRows(boolean paged, int start) throws Exception {
		int tracker = start;
		int lTracker = tracker;
		int resultFieldsLength = resultFields.length;
		spl(query);
		statement = connection.prepareStatement(query.toString());
		for (int i = 0; i < queryValues.length; i++) {
			statement.setString((i + 1), queryValues[i].toString());
		}
		resultSet = statement.executeQuery();
		boolean prevCustomRow = true;
		while (resultSet.next()) {
			tracker++;
			lTracker++;
			rowTag = new StringBuffer("");
			rowClass = new StringBuffer("");
			if (paged) {
				rowClass = new StringBuffer("srow r" + tracker);
			}
			if (resultSet.isFirst()) {
				applicationId = resultSet.getString(1);
			}
			if (!prevCustomRow) {
				lTracker--;
				prevCustomRow = true;
			}
			if (customRow(tracker)) {
				if (nob(rowTag.toString()))
					rowTag = new StringBuffer("class=\"" + rowClass + "\"");
				else
					rowTag.append("class=\"" + rowClass + "\"");
				rows.append("<tr " + rowTag + ">");
				rows.append("<td>" + lTracker + "</td>");
				for (int i = 0; i < resultFieldsLength; i++) {
					String value = resultSet.getString(i + 1);
					colTag = nct(value);
					colHtml = nc(value);
					customCol(tracker, i + 1, colHtml.toString());
					rows.append("<td " + colTag + ">" + colHtml + "</td>");
				}
				rows.append("</tr>");
			} else {
				prevCustomRow = false;
			}
			rowTag = new StringBuffer("");
		}
		return lTracker;
	}

	private void customCol(int row, int col, String value) throws Exception {
		if (customizeString.equals("<something>")) {
			if (col == 1) {
				if (value.equals("<something>"))
					colHtml = new StringBuffer("<something>");
				else
					colHtml = new StringBuffer("<something>");
			}
		}
	}

	private boolean customRow(int row) throws Exception {
		boolean allow = true;
		if (customizeString.equals("<something>")) {
			rowTag.append("onclick=\"javascript:pullDetails(this.id);\" style=\"cursor:pointer;\" class=\"psrow\" psid=\""+resultSet.getString(4)+"\" id=\"ps-id-"+resultSet.getString(4) + "\"");
		}
		else if (customizeString.equals("mis-reports")) {
			rowTag.append("onclick=\"javascript:pullDetails("+resultSet.getString(1)+");\" style=\"cursor:pointer;\" class=\"psrow\" psid=\""+resultSet.getString(1)+"\" id=\"ps-id-"+resultSet.getString(1) + "\"");
		}
		else if(customizeString.equals("searchPACNumber")){
			rowTag.append("onclick=\"javascript:pullDetails('"+URLEncoder.encode(resultSet.getString(6), "UTF-8")+"');\" style=\"cursor:pointer;\" class=\"psrow\" psid=\""+resultSet.getString(1)+"\" id=\"ps-id-"+resultSet.getString(1) + "\"");
		}
		return allow;
	}

	private String customField(int row, int col, String value) throws Exception {
		if (customizeString.equals("<something>")) {
			if (col == 5) {
				if (value.equals("<something>"))
					value = "<something>";
			}
		}
		return value;
	}

	public void qa(String v1) { // QUICK ADD - List1 (only null check)
		list1.add(new List1(nc(v1)));
	}

	public void qa(String v1, String v2) { // QUICK ADD - List2 (only null
												// check)
		list2.add(new List2(nc(v1), nc(v2)));
	}

	public void qa(StringBuffer label, StringBuffer value) { // QUICK ADD -
																// List2
																// (comprehensive
																// checks)
		list2.add(new List2(ncl(label, value), ync(nc(value.toString()))));
	}

	public void qa(String v1, String v2, String v3) { // QUICK ADD - List3
															// (only null check)
		list3.add(new List3(nc(v1), nc(v2), nc(v3)));
	}

	public void qa(String v1, String v2, String v3, String v4) { // QUICK ADD
																	// - List4
																	// (only
																	// null
																	// check)
		list4.add(new List4(nc(v1), nc(v2), nc(v3), nc(v4)));
	}

	public void qa(String v1, String v2, String v3, String v4, String v5) { // QUICK
																				// ADD
																				// -
																				// List5
																				// (only
																				// null
																				// check)
		list5.add(new List5(nc(v1), nc(v2), nc(v3), nc(v4), nc(v5)));
	}
	public long getIstOffset() {
		return istOffset;
	}
	public void setIstOffset(long istOffset) {
		this.istOffset = istOffset;
	}
	public long getLocalOffset() {
		return localOffset;
	}
	public void setLocalOffset(long localOffset) {
		this.localOffset = localOffset;
	}
	public List<Notification> getNotifyList() {
		return notifyList;
	}
	public void setNotifyList(List<Notification> notifyList) {
		this.notifyList = notifyList;
	}
	// END - COMMON FOR ALL
	
}
