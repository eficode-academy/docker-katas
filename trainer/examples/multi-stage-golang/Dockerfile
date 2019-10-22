FROM golang:1.13.2 as builder
WORKDIR /go
COPY hello.go /go
RUN go build hello.go

FROM scratch
COPY --from=builder /go/hello /hello
CMD ["/hello"]
