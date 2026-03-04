import javax.sound.sampled.*; // 오디오 관련 클래스를 쓰기 위해 import
import java.io.File; // 파일경로로 음악 파일을 열기 위해 import

// BGM 재생 기능을 모아둔 클래스
public class BGMPlayer {
    private static Clip clip;
    // path 경로에 있는 음원을 재생하는 메소드
    public static void play(String path) {
        try { // try-catch 구문 사용
            if (clip != null) { // 기존 BGM이 있으면 
                clip.stop(); // 재생을 멈추고 
                clip.close(); // 닫아서 새 파일을 열음
            }
            // path 파일을 읽어서 음원 형태로 가져옴
            AudioInputStream ais = AudioSystem.getAudioInputStream(new File(path));
            clip = AudioSystem.getClip(); // 객체 생성해서 clip에 넣음
            clip.open(ais); //  clip에 음원 데이터를 넣음

            clip.loop(Clip.LOOP_CONTINUOUSLY); // 음악 반복 재생
            clip.start(); // 재생 시작

        } catch (Exception e) {
            e.printStackTrace(); // 콘솔에 오류 내용 출력
        }
    }

    // 재생중인지 확인
    public static boolean isPlaying() {
        return clip != null && clip.isRunning(); // clip이 존재하고, 재생중이면 true 반환
    }

    // ON/OFF 토글
    public static void toggle() {
        if (clip == null) return;

        if (clip.isRunning()) {
            clip.stop(); // 지금 켜져 있으면 → 끔
        } else {
            clip.start(); // 지금 꺼져 있으면 → 다시 켬
        }
    }
}
