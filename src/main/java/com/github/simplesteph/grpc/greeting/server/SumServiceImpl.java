package com.github.simplesteph.grpc.greeting.server;

import com.proto.sum.*;
import io.grpc.stub.StreamObserver;

public class SumServiceImpl extends SumServiceGrpc.SumServiceImplBase {

    @Override
    public void sum(SumRequest request, StreamObserver<SumResponse> responseObserver) {

        Sum sum = request.getSum();
        int sumResult = sum.getFirstNumber() + sum.getSecondNumber();

        SumResponse sumResponse = SumResponse.newBuilder()
                .setSum(sumResult)
                .build();

        responseObserver.onNext(sumResponse);

        responseObserver.onCompleted();
    }

        @Override
        public void getPrimeNumberDecomposition(PrimeNumberRequest request, StreamObserver<PrimeNumberResponseStream> responseObserver) {
            int primeNumber = request.getPrimeNumber().getNumber();

            int k = 2;

            try{
                while(primeNumber > 1){
                    if(primeNumber % k == 0){
                        responseObserver.onNext(PrimeNumberResponseStream.newBuilder().setResult(k).build());
                        primeNumber = primeNumber / k;
                    }else{
                        k += 1;
                    }

                    Thread.sleep(1000L);
                }
            }catch (InterruptedException e){
                e.printStackTrace();
            }finally {
                responseObserver.onCompleted();

            }

        }

    @Override
    public StreamObserver<AvgNumberRequestStream> getAvgNumber(StreamObserver<AvgNumberResponse> responseObserver) {


        StreamObserver<AvgNumberRequestStream> requestStreamStreamObserver = new StreamObserver<AvgNumberRequestStream>() {

            int numberOfIncomingMessage = 0;
            int total = 0;

            @Override
            public void onNext(AvgNumberRequestStream value) {
                // client sends a message
                System.out.println("message received: " + value.getNumber());
                total += value.getNumber();
                numberOfIncomingMessage += 1;
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {
                double avg = (double) total / numberOfIncomingMessage;
                responseObserver.onNext(AvgNumberResponse.newBuilder()
                        .setResult(avg)
                        .build());

                responseObserver.onCompleted();
            }
        };

        return requestStreamStreamObserver;

    }
}
