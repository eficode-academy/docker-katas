FROM golang:latest as build

RUN mkdir /app
ADD . /app/
WORKDIR /app
RUN go build -o main .

FROM scratch
COPY --from=build /app/main /
CMD ["/main"]
