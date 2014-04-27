package sound;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;
import org.lwjgl.util.WaveData;

public class Sound {
	  
		/** Buffers hold sound data. */
	  int buffer;

	  /** Sources are points emitting sound. */
	  public int source;
	  
	  /** Position of the source sound. */
	  FloatBuffer sourcePos = BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f });

	  /*
	   * These are 3D cartesian vector coordinates. A structure or class would be
	   * a more flexible of handling these, but for the sake of simplicity we will
	   * just leave it as is.
	   */  
	  
	  /** Velocity of the source sound. */
	  FloatBuffer sourceVel = BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f });

	  /** Position of the listener. */
	  FloatBuffer listenerPos = BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f });

	  /** Velocity of the listener. */
	  FloatBuffer listenerVel = BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f });

	  /** Orientation of the listener. (first 3 elements are "at", second 3 are "up")
	      Also note that these should be units of '1'. */
	  FloatBuffer listenerOri = BufferUtils.createFloatBuffer(6).put(new float[] { 0.0f, 0.0f, -1.0f,  0.0f, 1.0f, 0.0f });  

	public Sound(String fileName){
	    sourcePos.flip();
	    sourceVel.flip();
	    listenerPos.flip();
	    listenerVel.flip();
	    listenerOri.flip();

	    // Load the wav data.
	    if(loadALData(fileName) == AL10.AL_FALSE) {
	      System.out.println("Error loading data.");
	      return;
	    }
	}

	  /**
	   * boolean LoadALData()
	   *
	   *  This function will load our sample data from the disk using the Alut
	   *  utility and send the data into OpenAL as a buffer. A source is then
	   *  also created to play that buffer.
	   */
	  int loadALData(String file) {
	    // Load wav data into a buffer.
	    buffer = AL10.alGenBuffers();

	    if(AL10.alGetError() != AL10.AL_NO_ERROR)
	      return AL10.AL_FALSE;

	    FileInputStream fin = null;
	    BufferedInputStream bin = null;
	    try
	    {
	        fin = new FileInputStream("res/" + file);
	        bin = new BufferedInputStream(fin);
	    }
	    catch(FileNotFoundException e)
	    {
	        e.printStackTrace();
	    }
	    WaveData waveFile = WaveData.create(bin);
	    AL10.alBufferData(buffer, waveFile.format, waveFile.data, waveFile.samplerate);

	    waveFile.dispose();

	    // Bind the buffer with the source.
	    source = AL10.alGenSources();

	    if(AL10.alGetError() != AL10.AL_NO_ERROR)
	      return AL10.AL_FALSE;

	    AL10.alSourcei(source, AL10.AL_BUFFER,   buffer );
	    AL10.alSourcef(source, AL10.AL_PITCH,    1.0f          );
	    AL10.alSourcef(source, AL10.AL_GAIN,     1.0f          );
	    AL10.alSource (source, AL10.AL_POSITION, sourcePos     );
	    AL10.alSource (source, AL10.AL_VELOCITY, sourceVel     );

	    // Do another error check and return.
	    if(AL10.alGetError() == AL10.AL_NO_ERROR)
	      return AL10.AL_TRUE;

	    return AL10.AL_FALSE;
	  }  
	  
	  /**
	   * void setListenerValues()
	   *
	   *  We already defined certain values for the Listener, but we need
	   *  to tell OpenAL to use that data. This function does just that.
	   */
	  void setListenerValues() {
	    AL10.alListener(AL10.AL_POSITION,    listenerPos);
	    AL10.alListener(AL10.AL_VELOCITY,    listenerVel);
	    AL10.alListener(AL10.AL_ORIENTATION, listenerOri);
	  }  

	  /**
	   * void killALData()
	   *
	   *  We have allocated memory for our buffers and sources which needs
	   *  to be returned to the system. This function frees that memory.
	   */
	  void killALData() {
	    AL10.alDeleteSources(source);
	    AL10.alDeleteBuffers(buffer);
	  }  
}
