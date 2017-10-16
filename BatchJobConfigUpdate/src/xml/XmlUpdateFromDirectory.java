package xml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;


/**
 *<p>This class have been implemented to update job configuration files
 *(*.script.xml and *.template.xml) from FM01, FM02 batch jobs.
 *</p>
 * @author Santosh Kumar Dubey
 */
public class XmlUpdateFromDirectory
{
    //~ Methods ----------------------------------------------------------------

    /**
    *
    * <p>
    *    This method prints the original data from the job configuration file (*.script.xml and *.template.xml).
    *</p>
    *
    * @param targetFilePath
    * @param jobName
    *
    * @throws IOException
    */
    private static void verifyScriptAndTemplateXMLOriginalData(
        String targetFilePath, String jobName )
      throws IOException
    {
        File targetFile = new File( targetFilePath );
        String line;

        for ( File f : targetFile.listFiles( ) )
        {
            if ( f.isFile( ) )
            {
                if ( f.getName( ).equals( jobName + templateFileExtension ) )
                {
                    BATCH_JOB_CONTENTS =
                        new BufferedReader( new FileReader(
                                targetFilePath + jobName +
                                  templateFileExtension ) );

                    while ( ( line = BATCH_JOB_CONTENTS.readLine( ) ) != null )
                    {
                        if ( line.contains( originalString ) &&
                              StringUtils.startsWith( line.trim( ), tag ) )
                        {
                            System.out.format( format, timeStamp, jobName,
                                jobName + templateFileExtension, line );
                        }
                    }
                }
                else
                {
                    if ( f.getName( ).equals( jobName + scriptFileExtension ) )
                    {
                        BATCH_JOB_CONTENTS =
                            new BufferedReader( new FileReader(
                                    targetFilePath + jobName +
                                      scriptFileExtension ) );

                        while ( ( line = BATCH_JOB_CONTENTS.readLine( ) ) !=
                              null )
                        {
                            if ( line.contains( originalString ) &&
                                  StringUtils.startsWith( line.trim( ), tag ) )
                            {
                                System.out.format( format, timeStamp, jobName,
                                    jobName + scriptFileExtension, line );
                            }
                        }
                    }
                }
            }
        }
    }


    /**
     *
     * <p>
     *    This method verifies changes from the job configuration file (*.script.xml and *.template.xml).
     *</p>
     *
     * @param targetFilePath
     * @param jobName
     *
     * @throws IOException
     */
    private static void verifyScriptAndTemplateXMLChanges(
        String targetFilePath, String jobName )
      throws IOException
    {
        File targetFile = new File( targetFilePath );
        String line;

        for ( File f : targetFile.listFiles( ) )
        {
            if ( f.isFile( ) )
            {
                if ( f.getName( ).equals( jobName + templateFileExtension ) )
                {
                    BATCH_JOB_CONTENTS =
                        new BufferedReader( new FileReader(
                                targetFilePath + jobName +
                                  templateFileExtension ) );

                    while ( ( line = BATCH_JOB_CONTENTS.readLine( ) ) != null )
                    {
                        if ( line.contains( updateToString ) &&
                              StringUtils.startsWith( line.trim( ), tag ) )
                        {
                            System.out.format( format, timeStamp, jobName,
                                jobName + templateFileExtension, line );
                        }
                    }
                }
                else
                {
                    if ( f.getName( ).equals( jobName + scriptFileExtension ) )
                    {
                        BATCH_JOB_CONTENTS =
                            new BufferedReader( new FileReader(
                                    targetFilePath + jobName +
                                      scriptFileExtension ) );

                        while ( ( line = BATCH_JOB_CONTENTS.readLine( ) ) !=
                              null )
                        {
                            if ( line.contains( updateToString ) &&
                                  StringUtils.startsWith( line.trim( ), tag ) )
                            {
                                System.out.format( format, timeStamp, jobName,
                                    jobName + scriptFileExtension, line );
                            }
                        }
                    }
                }
            }
        }
    }


    /**
     * <p>
     *    This is runner method which forms *.script.xml and *.template.xml directory path
     *    and verifies the original data from the job configuration.
     * </p>
     *
     * @param jobList
     *
     * @throws IOException
     */
    public static void verifyOriginalDataFromJobConfigurationFile(
        List<String> jobList )
      throws IOException
    {
        for ( String job : jobList )
        {
            for ( File f : dirList.listFiles( ) )
            {
                if ( f.isDirectory( ) )
                {
                    if ( f.getName( ).equals( job ) )
                    {
                        verifyScriptAndTemplateXMLOriginalData(
                            absoluteJobDirPath + "\\" + job + "\\", job );
                    }
                }
            }
        }
    }


    /**
     * <p>
     *    This is runner method which forms *.script.xml and *.template.xml directory path
     *    and verifies the changes from the job configuration.
     * </p>
     *
     * @param jobList
     *
     * @throws IOException
     */
    public static void verifyChangesFromJobConfigurationFile(
        List<String> jobList )
      throws IOException
    {
        for ( String job : jobList )
        {
            for ( File f : dirList.listFiles( ) )
            {
                if ( f.isDirectory( ) )
                {
                    if ( f.getName( ).equals( job ) )
                    {
                        verifyScriptAndTemplateXMLChanges( absoluteJobDirPath +
                              "\\" + job + "\\", job );
                    }
                }
            }
        }
    }


    /**
     * <p>
     *  This method will update the *.script.xml job configuration files.
     * </p>
     *
     * @param targetFilePath
     * @param jobName
     *
     * @throws IOException
     */

    private static boolean updateScriptXML( String targetFilePath,
        String jobName )
      throws IOException
    {
        String line;
        boolean isUpdated = false;
        boolean isAlreadyUpdated = false;

        if ( !StringUtils.isEmpty( targetFilePath ) &&
              !StringUtils.isEmpty( jobName ) )
        {
            BATCH_JOB_CONTENTS =
                new BufferedReader( new FileReader(
                        targetFilePath + jobName + scriptFileExtension ) );

            List<String> lines = new ArrayList<String>( );
            int index = 0;

            while ( ( line = BATCH_JOB_CONTENTS.readLine( ) ) != null )
            {
                if ( line.contains( originalString ) &&
                      StringUtils.startsWith( line.trim( ), tag ) )
                {
                    line = line.replaceAll( originalString, updateToString );
                }
                else if ( line.contains( updateToString ) )
                {
                    isAlreadyUpdated = true;
                }

                lines.add( index++, line );
            }

            BATCH_JOB_CONTENTS.close( );

            if ( !isAlreadyUpdated )
            {
                File oldFile =
                    new File( targetFilePath + "\\" + jobName +
                          scriptFileExtension );

                oldFile.setWritable( true, true );

                isUpdated =
                    writeToFile( oldFile, lines, "UTF-8" ) ? true : false;
            }
        }

        return isUpdated;
    }


    /**
     * <p>
     *  This method will update the *.template.xml job configuration files.
     * </p>
     *
     * @param targetFilePath
     *
     * @param jobName
     *
     * @throws IOException
     *
     */
    private static boolean updateTemplateXML( String targetFilePath,
        String jobName )
      throws IOException
    {
        String line;
        boolean isUpdated = false;
        boolean isAlreadyUpdated = false;

        if ( !StringUtils.isEmpty( targetFilePath ) &&
              !StringUtils.isEmpty( jobName ) )
        {
            BATCH_JOB_CONTENTS =
                new BufferedReader( new FileReader(
                        targetFilePath + jobName + templateFileExtension ) );

            List<String> lines = new ArrayList<String>( );
            int index = 0;

            while ( ( line = BATCH_JOB_CONTENTS.readLine( ) ) != null )
            {
                if ( line.contains( originalString ) &&
                      StringUtils.startsWith( line.trim( ), tag ) )
                {
                    line = line.replaceAll( originalString, updateToString );
                }
                else if ( line.contains( updateToString ) )
                {
                    isAlreadyUpdated = true;
                }

                lines.add( index++, line );
            }

            BATCH_JOB_CONTENTS.close( );

            if ( !isAlreadyUpdated )
            {
                File oldFile =
                    new File( targetFilePath + "\\" + jobName +
                          templateFileExtension );

                oldFile.setWritable( true, true );

                isUpdated =
                    writeToFile( oldFile, lines, "UTF-8" ) ? true : false;
            }
        }

        return isUpdated;
    }


    /**
     * <p>
     *    This is runner method to update *.script.xml and *.template.xml files.
     *    <ul><u><b>Conditions: </b></u>
     *      <li>If the job directory is not present in the destination path then show it to user</li>
     *      <li>Check if the job has already updated with current changes</li>
     *      <li>If the few jobs are updated already and few not then program will handle this condition accordingly</li>
     *      <li>If the destination job directory has *.script.xml then update this otherwise update both</li>
     *    </ul>
     * </p>
     *
     * @param  jobList
     *
     * @throws FileNotFoundException, IOException
     *
     * @return Map
     *
     */
    public static Map<String, ArrayList<String>> doChangesInJobConfigurationFile(
        List<String> jobList )
      throws IOException, FileNotFoundException
    {
        boolean isUpdated = true;
        Map<String, ArrayList<String>> jobProcessStatusMap =
            new LinkedHashMap<String, ArrayList<String>>( );
        int numberOfFiles = 0;

        for ( String job : jobList )
        {
            for ( File f : dirList.listFiles( ) )
            {
                numberOfFiles =
                    ( !jobProcessStatusMap.containsKey( f.getName( ) ) )
                    ? f.list( ).length : 0;

                if ( f.isDirectory( ) && job.equals( f.getName( ) ) &&
                      ( numberOfFiles != 0 ) &&
                      !jobProcessStatusMap.containsKey( f.getName( ) ) )
                {
                    for ( File jobFile : f.listFiles( ) )
                    {
                        if ( jobFile.isFile( ) && ( numberOfFiles != 0 ) )
                        {
                            isUpdated =
                                (
                                    jobFile.getName( ).equals(
                                        job + templateFileExtension )
                                    )
                                ? (
                                    updateTemplateXML(
                                        absoluteJobDirPath + "\\" + job + "\\",
                                        job )
                                    )
                                : (
                                    updateScriptXML(
                                        absoluteJobDirPath + "\\" + job + "\\",
                                        job )
                                    );

                            if ( !isUpdated )
                            {
                                if (
                                    !jobProcessStatusMap.containsKey(
                                          f.getName( ) ) )
                                {
                                    jobProcessStatusMap.put( job,
                                        new ArrayList<String>( ) );
                                }

                                jobProcessStatusMap.get( job ).add(
                                    jobFile.toString( ) + ": " +
                                      "already updated" );
                            }
                            else
                            {
                                if (
                                    !jobProcessStatusMap.containsKey(
                                          f.getName( ) ) )
                                {
                                    jobProcessStatusMap.put( job,
                                        new ArrayList<String>( ) );
                                }

                                jobProcessStatusMap.get( job ).add(
                                    jobFile.toString( ) + ": " + "updated" );
                            }
                        }

                        numberOfFiles--;
                    }

                    break;
                }

                isUpdated = false;
            }
        }

        return jobProcessStatusMap;
    }


    /**
     * <p>
     *  This method will overwrite/create the *.script.xml and *.template.xml files with the new changes.
     * </p>
     *
     * @param file
     * @param lines
     * @param encoding
     * @return Success
     */
    private static boolean writeToFile( File file, List<String> lines,
        String encoding )
    {
        boolean success = false;

        try
        {
            PrintWriter writer = new PrintWriter( file, encoding );

            for ( String line : lines )
            {
                writer.println( line );
            }

            writer.close( );
            success = true;
        }
        catch ( Exception e )
        {
            e.printStackTrace( );
        }

        return success;
    }


    /**
     * <p>This is the method where all jobs (that need to be updated) will be added to a list.
     *    <b>Only the stream directory should be added to the list not configuration file name.</b>
     *
     *  </p>
     *
    * @return List
    */
    private static List<String> feedInput( )
    {
        List<String> list = new ArrayList<String>( );


        list.add( "ABC-000-000" );


        return list;
    }


    /**
     * <p> This is main method which process the changes.</p>
     *
     * @param args
     * @throws IOException
     *
     */
    public static void main( String[] args )
      throws IOException
    {
        List<String> streamList = feedInput( );

        FileOutputStream f =
            new FileOutputStream(
                "..//BatchXmlConfigurationUpdate//src//log//log.txt" );
        System.setOut( new PrintStream( f ) );

        System.out.println( "Pre-Process (Original Data) Verification:" );
        System.out.println(
            "-----------------------------------------------------------------------------------------------------------------------------------------------" );
        verifyOriginalDataFromJobConfigurationFile( streamList );
        System.out.println( );

        
        System.out.println( "Processing Changes:" );
        System.out.println(
            "-----------------------------------------------------------------------------------------------------------------------------------------------" );

        Map<String, ArrayList<String>> jobProcessStatusMap =
            doChangesInJobConfigurationFile( streamList );

        for ( Entry<String, ArrayList<String>> status :
              jobProcessStatusMap.entrySet( ) )
        {
            ArrayList<String> jobList = status.getValue( );

            for ( String job : jobList )
            {
                System.out.format( formatForMain, timeStamp, status.getKey( ),
                    job );
            }
        }

        
        System.out.println( "\nPost-Process Verification:" );
        System.out.println(
            "-----------------------------------------------------------------------------------------------------------------------------------------------" );

        verifyChangesFromJobConfigurationFile( streamList );
        f.close( );
        ps.println(
            "Log file Generated(/BatchXmlConfigurationUpdate/src/log/log.txt)..." );
        ps.println( "Please refresh log directory..." );
    }

    //~ Static variables -------------------------------------------------------

    /**
     * Value of this static variable will be the original string from the job configuration file.
     */
    private static String originalString = "ABC";

    /**
     * Value of this static variable will be the new string value which we are intended
     * to update in the job configuration file.
     */
    private static String updateToString = "ABC.xml";

    /**
     * Value of this static variable will be the tag name where you want to make changes.
     */
    private static String tag = "tag1";

    /**
     * Value of this static variable will be the location of the job configuration.
     *  
     */
    
    private static String absoluteJobDirPath = "C:\\rad_workspaces\\sample_for_delete";
    private static File dirList = new File( absoluteJobDirPath );
    private static String timeStamp =
        new SimpleDateFormat( "yyyy/MM/dd HH:mm:ss", Locale.US ).format(
            new Date( ) );
    private static BufferedReader BATCH_JOB_CONTENTS = null;
    private static String templateFileExtension = "-template.xml";
    private static String scriptFileExtension = "-script.xml";
    private static PrintStream ps = System.out;
    private static String format = "%1$-10s | %2$-5s | %3$-35s\t\t | %4$-30s\n";
    private static String formatForMain = "%1$-10s | %2$-5s | %3$-35s\t\t\n";
}
