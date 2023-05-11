module com.example.auctionchat {
        // 다른 모듈들의 requires 문이 위치합니다.
        requires java.base;
        opens java.time;
        opens com.example.auctionchat;
        opens com.example.auctionchat.mongomodel;
}