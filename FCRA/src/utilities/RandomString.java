package utilities;


public class RandomString 
{
	String randomString="";
	final int _IDlength=32;
	final String validChars ="abcdefghjkmnpqrstABCDEFGHJKMNPQRSTUVWXYZ23456789@#$%*";
	final String validNumber ="0123456789";
	int maxIndex =0;
	
	public String getRandomString() 
	{
		maxIndex = validChars.length();
		java.util.Random rnd = new java.security.SecureRandom();

		for ( int i = 0 ; i < _IDlength ; i++ ) 
		{
			int rndPos = Math.abs(rnd.nextInt() % maxIndex);   
			randomString += validChars.charAt(rndPos);
		}
		return randomString;
	}
	
	public String getRandomString(Integer length) 
	{
		maxIndex = validChars.length();
		java.util.Random rnd = new java.security.SecureRandom();

		for ( int i = 0 ; i < length ; i++ ) 
		{
			int rndPos = Math.abs(rnd.nextInt() % maxIndex);   
			randomString += validChars.charAt(rndPos);
		}
		return randomString;
	}
	public String getRandomNumber(Integer length) 
	{
		maxIndex = validNumber.length();
		java.util.Random rnd = new java.security.SecureRandom();

		for ( int i = 0 ; i < length ; i++ ) 
		{
			int rndPos = Math.abs(rnd.nextInt() % maxIndex);   
			randomString += validNumber.charAt(rndPos);
		}
		return randomString;
	}

}
