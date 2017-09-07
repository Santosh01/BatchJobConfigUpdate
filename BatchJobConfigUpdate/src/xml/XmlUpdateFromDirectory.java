package xml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;


/**
 *<p>This class have been implemented to update job configuration files
 *(*.test.xml and *.test.template.xml) from 01, 02 batch jobs.
 *</p>
 * @author Santosh Kumar Dubey
 */
public class XmlUpdateFromDirectory
{
    //~ Methods ----------------------------------------------------------------

    /**
     *
     * <p>
     *    This method verifies changes from the job configuration file (*.test.xml and *.test.template.xml).
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
                            System.out.println( jobName + ":" + line + " in " +
                                  jobName + templateFileExtension );
                        }
                    }
                }
                else
                {
                    if ( f.getName( ).equals( jobName + testFileExtension ) )
                    {
                        BATCH_JOB_CONTENTS =
                            new BufferedReader( new FileReader(
                                    targetFilePath + jobName +
                                      testFileExtension ) );

                        while ( ( line = BATCH_JOB_CONTENTS.readLine( ) ) !=
                              null )
                        {
                            if ( line.contains( updateToString ) &&
                                  StringUtils.startsWith( line.trim( ), tag ) )
                            {
                                System.out.println( jobName + ":" + line +
                                      " in " + jobName + testFileExtension );
                            }
                        }
                    }
                }
            }
        }
    }


    /**
     * <p>
     *    This is runner method which forms *.test.xml and *.test.template.xml directory path
     *    and verifies the changes from the job configuration.
     * </p>
     *
     * @param jobList
     *
     * @throws IOException
     */
    public static void verifyChangesFromJobConfigurationFile( List<String> jobList )
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
     *  This method will update the *.test.xml job configuration files.
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
                        targetFilePath + jobName + testFileExtension ) );

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
                          testFileExtension );

                oldFile.setWritable( true, true );

                isUpdated =
                    writeToFile( oldFile, lines, "UTF-8" ) ? true : false;
            }
        }

        return isUpdated;
    }


    /**
     * <p>
     *  This method will update the *.test.template.xml job configuration files.
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
     *    This is runner method to update *.test.xml and *.test.template.xml files.
     *    <ul><u><b>Conditions: </b></u>
     *      <li>If the job directory is not present in the destination path then show it to user</li>
     *      <li>Check if the job has already updated with current changes</li>
     *      <li>If the few jobs are updated already and few not then program will handle this condition accordingly</li>
     *      <li>If the destination job directory has *.test.xml then update this otherwise update both</li>
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
                else
                {
                    if ( !job.equals( f.getName( ) ) &&
                          !jobProcessStatusMap.containsKey( f.getName( ) ) )
                    {
                        if ( !jobProcessStatusMap.containsKey( f.getName( ) ) )
                        {
                            jobProcessStatusMap.put( job,
                                new ArrayList<String>( ) );
                        }

                        jobProcessStatusMap.get( job ).add(
                            "job directory not found" );
                    }
                }

                isUpdated = false;
            }
        }

        return jobProcessStatusMap;
    }


    /**
     * <p>
     *  This method will overwrite/create the *.test.xml and *.test.template.xml files with the new changes.
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
     * <p>This is main method where all jobs (that need to be updated) will be added to a list.
     *    <b>Only the stream directory should be added to the list not configuration file name.</b>
     *
     *  </p>
     *
     * @param args
     * @throws IOException
     *
     */
    public static void main( String[] args )
      throws IOException
    {
        List<String> list = new ArrayList<String>( );

        list.add( "ABC-01" );
        list.add( "XYZ-01" );

        Map<String, ArrayList<String>> jobProcessStatusMap = doChangesInJobConfigurationFile( list );

        for ( Entry<String, ArrayList<String>> status :
              jobProcessStatusMap.entrySet( ) )
        {
            ArrayList<String> jobList = status.getValue( );

            for ( String job : jobList )
            {
                System.out.println( status.getKey( ) + ": " + job );
            }
        }
        verifyChangesFromJobConfigurationFile( list );
    }

    //~ Static variables -------------------------------------------------------

    /**
     * Value of this static variable will the original string from the job configuration file.
     */
    private static String originalString = "";

    /**
     * Value of this static variable will be the new string value which we are intended
     * to update in the job configuration file.
     */
    private static String updateToString = "";

    private static String templateFileExtension = "-test.template.xml";
    private static String testFileExtension = "-test.xml";
    private static String tag = "<import file=";
    private static BufferedReader BATCH_JOB_CONTENTS = null;

    /**
     * Value of this static variable will be the location of the job configuration.
     *  such as "C:\\workspace\\[stream]"
     */
    private static String absoluteJobDirPath =
        "C:\\workspaces\\sample_for_delete";
    private static File dirList = new File( absoluteJobDirPath );
}
